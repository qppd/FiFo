package com.qppd.fifov2.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.qppd.fifov2.Classes.Category;
import com.qppd.fifov2.Classes.CategoryExpense;
import com.qppd.fifov2.Classes.Expense;
import com.qppd.fifov2.Classes.Note;
import com.qppd.fifov2.Classes.Profile;
import com.qppd.fifov2.Classes.Saving;
import com.qppd.fifov2.Classes.User;
import com.qppd.fifov2.Tasks.ProfileTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "fifo_db";

    private static final String KEY_ID = "id";

    private static final String TABLE_USERS = "users";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";

    private static final String TABLE_PROFILES = "profiles";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_FIRSTNAME = "firstname";
    private static final String KEY_LASTNAME = "lastname";
    private static final String KEY_BIRTHDATE = "birthdate";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_INCOME = "income";
    private static final String KEY_TYPE = "type";

    private static final String TABLE_CATEGORIES = "categories";
    private static final String KEY_NAME = "name";

    private static final String TABLE_EXPENSES = "expenses";
    private static final String KEY_CATEGORY_ID = "category_id";
    private static final String KEY_PRODUCT = "product";
    private static final String KEY_PRICE = "price";
    private static final String KEY_PRIORITY = "priority";
    private static final String KEY_DATE = "created_at";

    private static final String TABLE_SAVINGS = "savings";
    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_CREATED = "created_at";

    private static final String TABLE_NOTES = "notes";
    private static final String KEY_TITLE = "title";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_DATETIME = "created_at";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_USERNAME + " VARCHAR(255) UNIQUE, "
                + KEY_PASSWORD + " VARCHAR(255)"
                + ")";
        db.execSQL(CREATE_USERS_TABLE);

        String CREATE_CATEGORIES_TABLE = "CREATE TABLE " + TABLE_CATEGORIES + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_NAME + " VARCHAR(255) UNIQUE"
                + ")";
        db.execSQL(CREATE_CATEGORIES_TABLE);

        String CREATE_EXPENSES_TABLE = "CREATE TABLE " + TABLE_EXPENSES + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_CATEGORY_ID + " VARCHAR(255), "
                + KEY_PRODUCT + " VARCHAR(255), "
                + KEY_PRICE + " INTEGER, "
                + KEY_PRIORITY + " INTEGER, "
                + KEY_DATE + " VARCHAR(255)"
                + ")";
        db.execSQL(CREATE_EXPENSES_TABLE);

        String CREATE_SAVINGS_TABLE = "CREATE TABLE " + TABLE_SAVINGS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_AMOUNT + " DOUBLE, "
                + KEY_CREATED + " VARCHAR(255)"
                + ")";
        db.execSQL(CREATE_SAVINGS_TABLE);

        String CREATE_PROFILES_TABLE = "CREATE TABLE " + TABLE_PROFILES + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_USER_ID + " INTEGER UNIQUE, "
                + KEY_FIRSTNAME + " TEXT NULL, "
                + KEY_LASTNAME + " TEXT NULL, "
                + KEY_BIRTHDATE + " VARCHAR(255) NULL, "
                + KEY_PHONE + " VARCHAR(255) NULL, "
                + KEY_INCOME + " DECIMAL NULL, "
                + KEY_TYPE + " INTEGER NULL"
                + ")";
        db.execSQL(CREATE_PROFILES_TABLE);

        String CREATE_NOTES_TABLE = "CREATE TABLE " + TABLE_NOTES + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_TITLE + " VARCHAR(255) UNIQUE, "
                + KEY_CONTENT + " TEXT, "
                + KEY_DATETIME + " VARCHAR(255)"
                + ")";
        db.execSQL(CREATE_NOTES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SAVINGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        onCreate(db);
    }


    public Map<String, Double[]> getTotalExpensesAndSavingsGroupedByMonthAndYear() {
        Map<String, Double[]> totalExpensesAndSavingsMap = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Query for expenses
        String expenseQuery = "SELECT substr(" + KEY_DATE + ", 1, 2) || '/' || substr(" + KEY_DATE + ", 7, 4) AS month_year, SUM(" + KEY_PRICE + ") AS total_expenses FROM " + TABLE_EXPENSES + " GROUP BY month_year";
        Cursor expenseCursor = db.rawQuery(expenseQuery, null);

        // Populate expenses map
        if (expenseCursor != null && expenseCursor.moveToFirst()) {
            do {
                String monthYear = expenseCursor.getString(expenseCursor.getColumnIndex("month_year"));
                double totalExpenses = expenseCursor.getDouble(expenseCursor.getColumnIndex("total_expenses"));
                totalExpensesAndSavingsMap.put(monthYear, new Double[]{totalExpenses, 0.0});
            } while (expenseCursor.moveToNext());
            expenseCursor.close();
        }

        // Query for savings
        String savingQuery = "SELECT substr(" + KEY_CREATED + ", 1, 2) || '/' || substr(" + KEY_CREATED + ", 7, 4) AS month_year, SUM(" + KEY_AMOUNT + ") AS total_savings FROM " + TABLE_SAVINGS + " GROUP BY month_year";
        Cursor savingCursor = db.rawQuery(savingQuery, null);

        // Populate savings map
        if (savingCursor != null && savingCursor.moveToFirst()) {
            do {
                String monthYear = savingCursor.getString(savingCursor.getColumnIndex("month_year"));
                double totalSavings = savingCursor.getDouble(savingCursor.getColumnIndex("total_savings"));
                if (totalExpensesAndSavingsMap.containsKey(monthYear)) {
                    Double[] values = totalExpensesAndSavingsMap.get(monthYear);
                    values[1] = totalSavings;
                    totalExpensesAndSavingsMap.put(monthYear, values);
                } else {
                    totalExpensesAndSavingsMap.put(monthYear, new Double[]{0.0, totalSavings});
                }
            } while (savingCursor.moveToNext());
            savingCursor.close();
        }

        db.close();
        return totalExpensesAndSavingsMap;
    }


    public boolean addSaving(Saving saving) {
        long result;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_AMOUNT, saving.getPrice());
        values.put(KEY_CREATED, saving.getDate());

        result = db.insert(TABLE_SAVINGS, null, values);
        db.close();

        return result != -1;
    }

    public ArrayList<Saving> getAllSavings(int savingId) {
        ArrayList<Saving> savingList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery;

        if (savingId == 0) {
            // If categoryId is 0, retrieve all expenses
            selectQuery = "SELECT * FROM " + TABLE_SAVINGS;
        } else {
            // Retrieve expenses with a specific category_id
            selectQuery = "SELECT * FROM " + TABLE_SAVINGS + " WHERE " + KEY_ID + " = ?";
        }

        Cursor cursor;
        if (savingId == 0) {
            cursor = db.rawQuery(selectQuery, null);
        } else {
            String[] whereArgs = {String.valueOf(savingId)};
            cursor = db.rawQuery(selectQuery, whereArgs);
        }

        if (cursor.moveToFirst()) {
            do {
                Saving saving = new Saving();
                saving.setId(cursor.getInt(0));
                saving.setPrice(cursor.getInt(1));
                saving.setDate(cursor.getString(2));
                savingList.add(saving);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return savingList;
    }

    public void deleteSaving(Saving saving) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SAVINGS, KEY_ID + " = ?",
                new String[]{String.valueOf(saving.getId())});
        db.close();
    }

    public double getCurrentMonthTotalSaving(int month) {
        double totalSaving = 0.0;
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {KEY_AMOUNT};
        String whereClause = "substr(" + KEY_CREATED + ", 1, 2) = ?";
        String[] whereArgs = {String.format("%02d", month)};

        Cursor cursor = db.query(TABLE_SAVINGS, columns, whereClause, whereArgs, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    double amount = cursor.getDouble(cursor.getColumnIndex(KEY_AMOUNT));
                    totalSaving += amount;
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        db.close();
        return totalSaving;
    }


    public boolean addNote(Note note) {
        long result;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, note.getTitle());
        values.put(KEY_CONTENT, note.getContent());
        values.put(KEY_DATETIME, note.getDatetime());

        result = db.insert(TABLE_NOTES, null, values);
        db.close();

        return result != -1;
    }

    public ArrayList<Note> getAllNotes() {
        ArrayList<Note> noteList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery;

        selectQuery = "SELECT * FROM " + TABLE_NOTES;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setId(cursor.getInt(0));
                note.setTitle(cursor.getString(1));
                note.setContent(cursor.getString(2));
                note.setDatetime(cursor.getString(3));
                noteList.add(note);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return noteList;
    }

    public void deleteNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTES, KEY_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
        db.close();
    }

    public boolean updateNote(Note note) {
        long result;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, note.getTitle());
        values.put(KEY_CONTENT, note.getContent());


        result = db.update(TABLE_NOTES, values, KEY_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
        db.close();

        return result != -1;
    }


    public boolean addProfile(Profile profile) {
        long result;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USER_ID, profile.getUid());
        values.put(KEY_FIRSTNAME, profile.getFirstname());
        values.put(KEY_LASTNAME, profile.getLastname());
        values.put(KEY_BIRTHDATE, profile.getBirthdate());
        values.put(KEY_PHONE, profile.getPhone());
        values.put(KEY_INCOME, profile.getIncome());
        values.put(KEY_TYPE, profile.getType());

        result = db.insert(TABLE_PROFILES, null, values);
        db.close();

        return result != -1;
    }

    public boolean updateProfile(Profile profile) {
        long result;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FIRSTNAME, profile.getFirstname());
        values.put(KEY_LASTNAME, profile.getLastname());
        values.put(KEY_BIRTHDATE, profile.getBirthdate());
        values.put(KEY_PHONE, profile.getPhone());
        values.put(KEY_INCOME, profile.getIncome());
        values.put(KEY_TYPE, profile.getType());

        result = db.update(TABLE_PROFILES, values, KEY_ID + " = ?",
                new String[]{String.valueOf(profile.getId())});
        db.close();

        return result != -1;
    }


    public Profile getProfile(int uid) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PROFILES, new String[]{KEY_ID, KEY_USER_ID,
                        KEY_FIRSTNAME, KEY_LASTNAME, KEY_BIRTHDATE, KEY_PHONE, KEY_INCOME, KEY_TYPE},
                KEY_ID + "=?",
                new String[]{String.valueOf(uid)}, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                Profile profile = new Profile(cursor.getInt(0), cursor.getInt(1),
                        cursor.getString(2), cursor.getString(3), cursor.getString(4),
                        cursor.getString(5), cursor.getString(6), cursor.getInt(7));
                cursor.close();
                return profile;
            } else {
                return null;
            }

        }

        return null;
    }

    public boolean checkPhoneExistsForUser(String phone, String userId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT COUNT(*) FROM " + TABLE_PROFILES +
                " WHERE " + KEY_PHONE + " = ? AND " + KEY_USER_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{phone, String.valueOf(userId)});

        boolean exists = false;
        if (cursor.moveToFirst()) {
            exists = cursor.getInt(0) > 0;
        }

        cursor.close();
        return exists;
    }









    public boolean addExpense(Expense expense) {
        long result;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CATEGORY_ID, expense.getCategory_id());
        values.put(KEY_PRODUCT, expense.getProduct());
        values.put(KEY_PRICE, expense.getPrice());
        values.put(KEY_PRIORITY, expense.getPriority());
        values.put(KEY_DATE, expense.getDate().toString());

        result = db.insert(TABLE_EXPENSES, null, values);
        db.close();

        return result != -1;
    }

    public ArrayList<Expense> getAllExpenses(int categoryId) {
        ArrayList<Expense> expenseList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery;

        if (categoryId == 0) {
            // If categoryId is 0, retrieve all expenses
            selectQuery = "SELECT * FROM " + TABLE_EXPENSES;
        } else {
            // Retrieve expenses with a specific category_id
            selectQuery = "SELECT * FROM " + TABLE_EXPENSES + " WHERE " + KEY_CATEGORY_ID + " = ?";
        }

        Cursor cursor;
        if (categoryId == 0) {
            cursor = db.rawQuery(selectQuery, null);
        } else {
            String[] whereArgs = {String.valueOf(categoryId)};
            cursor = db.rawQuery(selectQuery, whereArgs);
        }

        if (cursor.moveToFirst()) {
            do {
                Expense expense = new Expense();
                expense.setId(cursor.getInt(0));
                expense.setCategory_id(cursor.getInt(1));
                expense.setProduct(cursor.getString(2));
                expense.setPrice(cursor.getDouble(3));
                expense.setPriority(cursor.getInt(4));
                expense.setDate(cursor.getString(5));
                expenseList.add(expense);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return expenseList;
    }

    public Expense getExpense(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_EXPENSES, new String[]{KEY_ID,
                        KEY_CATEGORY_ID, KEY_PRODUCT, KEY_PRICE, KEY_PRIORITY, KEY_DATE}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Expense expense = new Expense(cursor.getInt(0), cursor.getString(1), cursor.getDouble(2), cursor.getInt(3), cursor.getString(4));
        cursor.close();
        return expense;
    }

    public ArrayList<Expense> getCurrentMonthPriorityTotalExpenses() {
        ArrayList<Expense> expenseList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_EXPENSES + " WHERE priority = 1 " +
                "AND substr(created_at, 1, 2) = ? AND substr(created_at, 7, 4) = ? " +
                "ORDER BY created_at ASC LIMIT 5 ";

        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH) + 1; // Months are zero-based in Calendar
        int currentYear = calendar.get(Calendar.YEAR);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.format("%02d", currentMonth), String.valueOf(currentYear)});

        if (cursor.moveToFirst()) {
            do {
                Expense expense = new Expense();
                expense.setId(cursor.getInt(0));
                expense.setCategory_id(cursor.getInt(1));
                expense.setProduct(cursor.getString(2));
                expense.setPrice(cursor.getDouble(3));
                expense.setPriority(cursor.getInt(4));
                expense.setDate(cursor.getString(5));
                expenseList.add(expense);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return expenseList;
    }

    public ArrayList<Expense> getAllExpenses() {
        ArrayList<Expense> expenseList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_EXPENSES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Expense expense = new Expense();
                expense.setId(cursor.getInt(0));
                expense.setCategory_id(cursor.getInt(1));
                expense.setProduct(cursor.getString(2));
                expense.setPrice(cursor.getDouble(3));
                expense.setPriority(cursor.getInt(4));
                expense.setDate(cursor.getString(5));
                expenseList.add(expense);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return expenseList;
    }

    public void updateExpense(Expense expense) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CATEGORY_ID, expense.getCategory_id());
        values.put(KEY_PRODUCT, expense.getProduct());
        values.put(KEY_PRICE, expense.getPrice());
        values.put(KEY_PRIORITY, expense.getPriority());
        values.put(KEY_DATE, expense.getDate());

        db.update(TABLE_EXPENSES, values, KEY_ID + " = ?",
                new String[]{String.valueOf(expense.getId())});
        db.close();
    }

    public void deleteExpense(Expense expense) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EXPENSES, KEY_ID + " = ?",
                new String[]{String.valueOf(expense.getId())});
        db.close();
    }

    public int getExpensesCount() {
        String countQuery = "SELECT * FROM " + TABLE_EXPENSES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public double getCurrentMonthTotalExpenses(int month) {
        double totalExpenses = 0.0;
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {KEY_PRICE};
        String whereClause = "substr(" + KEY_DATE + ", 1, 2) = ?";
        String[] whereArgs = {String.format("%02d", month)};

        Cursor cursor = db.query(TABLE_EXPENSES, columns, whereClause, whereArgs, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    double amount = cursor.getDouble(cursor.getColumnIndex(KEY_PRICE));
                    totalExpenses += amount;
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        db.close();
        return totalExpenses;
    }

    public ArrayList<CategoryExpense> getCategoriesWithTotalExpenseCurrentMonth() {
        ArrayList<CategoryExpense> categoryExpenseList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        // Get current month and year
        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH) + 1; // Months are zero-based in Calendar
        int currentYear = calendar.get(Calendar.YEAR);

        // Construct SQL query
        String query = "SELECT c.name, SUM(e.price) AS totalExpense " +
                "FROM categories c " +
                "LEFT JOIN expenses e ON c.id = e.category_id " +
                "WHERE substr(e.created_at, 1, 2) = ? AND substr(e.created_at, 7, 4) = ? " +
                "GROUP BY c.name";

        // Execute query
        Cursor cursor = db.rawQuery(query, new String[]{String.format("%02d", currentMonth), String.valueOf(currentYear)});

        // Iterate over the cursor and populate the list
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    CategoryExpense categoryExpense = new CategoryExpense();
                    categoryExpense.setCategory_name(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
                    categoryExpense.setExpense_total(cursor.getDouble(cursor.getColumnIndex("totalExpense")));
                    categoryExpenseList.add(categoryExpense);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        db.close();

        return categoryExpenseList;
    }

    public ArrayList<CategoryExpense> getCategoriesWithTotalExpenseCurrentMonthHighestToLowest() {
        ArrayList<CategoryExpense> categoryExpenseList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        // Get current month and year
        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH) + 1; // Months are zero-based in Calendar
        int currentYear = calendar.get(Calendar.YEAR);

        // Construct SQL query
        String query = "SELECT c.name, SUM(e.price) AS totalExpense " +
                "FROM categories c " +
                "LEFT JOIN expenses e ON c.id = e.category_id " +
                "WHERE substr(e.created_at, 1, 2) = ? AND substr(e.created_at, 7, 4) = ? " +
                "GROUP BY c.name ORDER BY totalExpense DESC";

        // Execute query
        Cursor cursor = db.rawQuery(query, new String[]{String.format("%02d", currentMonth), String.valueOf(currentYear)});

        // Iterate over the cursor and populate the list
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    CategoryExpense categoryExpense = new CategoryExpense();
                    categoryExpense.setCategory_name(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
                    categoryExpense.setExpense_total(cursor.getDouble(cursor.getColumnIndex("totalExpense")));
                    categoryExpenseList.add(categoryExpense);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        db.close();

        return categoryExpenseList;
    }


    public boolean addCategory(Category category) {
        long result;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, category.getName());

        result = db.insert(TABLE_CATEGORIES, null, values);
        db.close();

        return result != -1;
    }

    public Category getCategory(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CATEGORIES, new String[]{KEY_ID, KEY_NAME}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Category category = new Category(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
        cursor.close();
        return category;
    }

    public void updateCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, category.getName());

        db.update(TABLE_CATEGORIES, values, KEY_ID + " = ?",
                new String[]{String.valueOf(category.getId())});
        db.close();
    }

    public void deleteCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CATEGORIES, KEY_ID + " = ?",
                new String[]{String.valueOf(category.getId())});
        db.close();
    }

    public ArrayList<Category> getAllCategories() {
        ArrayList<Category> categoryList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_CATEGORIES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Category category = new Category();
                category.setId(Integer.parseInt(cursor.getString(0)));
                //category.setUser_id(String.valueOf(cursor.getInt(1)));
                category.setName(cursor.getString(1));
                categoryList.add(category);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return categoryList;
    }

    public int getCategoriesCount() {
        String countQuery = "SELECT * FROM " + TABLE_CATEGORIES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }


    public boolean addUser(User user) {
        long result;

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, user.getUsername());
        values.put(KEY_PASSWORD, user.getPassword());
        result = db.insert(TABLE_USERS, null, values);

        values = new ContentValues();
        values.put(KEY_USER_ID, user.getUsername());
        values.put(KEY_FIRSTNAME, "User");
        values.put(KEY_LASTNAME, "User");
        values.put(KEY_BIRTHDATE, "05/07/2024");
        values.put(KEY_PHONE, "09123456789");
        values.put(KEY_INCOME, 0);
        values.put(KEY_TYPE, 0);

        result = db.insert(TABLE_PROFILES, null, values);
        return result != -1;
    }

    public User getUser(String username) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USERS, new String[]{KEY_ID,
                        KEY_USERNAME, KEY_PASSWORD}, KEY_USERNAME + "=?",
                new String[]{username}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        User user = new User(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
        cursor.close();
        return user;
    }

    public boolean checkUserExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USERS, new String[]{KEY_ID,
                        KEY_USERNAME, KEY_PASSWORD}, KEY_USERNAME + "=?",
                new String[]{username}, null, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }
//
//    public void updateUser(User user) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(KEY_USERNAME, user.getUsername());
//        values.put(KEY_PASSWORD, user.getPassword());
//
//        db.update(TABLE_USERS, values, KEY_ID + " = ?",
//                new String[]{String.valueOf(user.getId())});
//        db.close();
//    }

    public boolean updateUser(User user) {
        long result;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, user.getUsername());
        values.put(KEY_PASSWORD, user.getPassword());

        result = db.update(TABLE_USERS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();

        return result != -1;
    }


    public boolean updatePassword(User user) {
        long result;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PASSWORD, user.getPassword());

        result = db.update(TABLE_USERS, values, KEY_USERNAME + " = ?",
                new String[]{String.valueOf(user.getUsername())});
        db.close();

        return result != -1;
    }


    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USERS, KEY_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }

    public int getUsersCount() {
        String countQuery = "SELECT * FROM " + TABLE_USERS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }
}

