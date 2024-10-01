package dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import data.base.DbHelper;
import model.Categories;

public class CategoriesDao {
    DbHelper dbHelper;

    public CategoriesDao(Context context) {
        dbHelper = new DbHelper(context);
    }

    //add Categories
    public int addCategories(int idShop, String name, byte[] image) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Kiểm tra xem danh mục có tồn tại không
        Cursor cursor = db.rawQuery("SELECT * FROM Categories WHERE name = ?", new String[]{name});
        if (cursor.getCount() > 0) {
            cursor.close();
            db.close();
            return 0; // Trả về 0 nếu danh mục đã tồn tại
        }

        // Nếu danh mục chưa tồn tại, thêm mới vào cơ sở dữ liệu
        ContentValues contentValues = new ContentValues();
        contentValues.put("idShop", idShop);
        contentValues.put("name", name);
        contentValues.put("image", image);

        long check = db.insert("Categories", null, contentValues);

        db.close();

        return (check == -1) ? -1 : 1; // Trả về -1 nếu thêm mới không thành công, ngược lại trả về 1
    }


    public boolean deleteCategories(int idCategories, int idShop) {
        try (SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase()) {
            int check = sqLiteDatabase.delete("Categories", "idCategories=? AND idShop=?", new String[]{String.valueOf(idCategories), String.valueOf(idShop)});
            if (check <= 0) {
                Log.e("CategoriesDao", "Failed to delete category with idCategories: " + idCategories + " and idShop: " + idShop);
                return false;
            }
            return true;
        } catch (SQLiteException e) {
            Log.e("CategoriesDao", "SQLiteException: " + e.getMessage());
            return false;
        }
    }


    public int getIdShop(int idUser) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        int idShop = -1;

        String[] columns = {"idShop"};
        String selection = "idUser = ?";
        String[] selectionArgs = {String.valueOf(idUser)};

        Cursor cursor = null;
        try {
            cursor = db.query("Shop", columns, selection, selectionArgs, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                idShop = cursor.getInt(cursor.getColumnIndexOrThrow("idShop"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return idShop;
    }

    public int getIdCategories(int idUser) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        int idCategories = -1;

        String[] columns = {"idCategories"};
        String selection = "idShop = ?";
        String[] selectionArgs = {String.valueOf(idCategories)};

        Cursor cursor = null;
        try {
            cursor = db.query("Categories", columns, selection, selectionArgs, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                idCategories = cursor.getInt(cursor.getColumnIndexOrThrow("idCategories"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return idCategories;
    }

    public List<Categories> getCategories(int idShop) {
        List<Categories> categoriesList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT idCategories, idShop, name, image FROM Categories WHERE idShop = ? OR idShop =1", new String[]{String.valueOf(idShop)});
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int idCategoriesIndex = cursor.getColumnIndex("idCategories");
                int idShopIndex = cursor.getColumnIndex("idShop");
                int nameIndex = cursor.getColumnIndex("name");
                int imageIndex = cursor.getColumnIndex("image");

                // Check if column indices are valid
                if (idCategoriesIndex >= 0 && idShopIndex >= 0 && nameIndex >= 0 && imageIndex >= 0) {
                    int idCategories = cursor.getInt(idCategoriesIndex);
                    int idShopValue = cursor.getInt(idShopIndex);
                    String name = cursor.getString(nameIndex);
                    byte[] imageBytes = cursor.getBlob(imageIndex);

                    Categories category = new Categories();
                    category.setIdCategories(idCategories);
                    category.setIdShop(idShopValue);
                    category.setName(name);
                    category.setImage(imageBytes);

                    categoriesList.add(category);
                }
            }
            cursor.close();
        }
        db.close();

        return categoriesList;
    }

    public List<Categories> getAllCategories() {
        List<Categories> categoriesList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT idCategories, idShop, name, image FROM Categories", null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int idCategoriesIndex = cursor.getColumnIndex("idCategories");
                int idShopIndex = cursor.getColumnIndex("idShop");
                int nameIndex = cursor.getColumnIndex("name");
                int imageIndex = cursor.getColumnIndex("image");

                // Check if column indices are valid
                if (idCategoriesIndex >= 0 && idShopIndex >= 0 && nameIndex >= 0 && imageIndex >= 0) {
                    int idCategories = cursor.getInt(idCategoriesIndex);
                    int idShopValue = cursor.getInt(idShopIndex);
                    String name = cursor.getString(nameIndex);
                    byte[] imageBytes = cursor.getBlob(imageIndex);

                    Categories category = new Categories();
                    category.setIdCategories(idCategories);
                    category.setIdShop(idShopValue);
                    category.setName(name);
                    category.setImage(imageBytes);

                    categoriesList.add(category);
                }
            }
            cursor.close();
        }
        db.close();

        return categoriesList;
    }


    public Categories getCategoryById(int idCategories) {
        Categories category = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT idCategories, idShop, name, image FROM Categories WHERE idCategories = ?", new String[]{String.valueOf(idCategories)});
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int idCategoriesIndex = cursor.getColumnIndex("idCategories");
                int idShopIndex = cursor.getColumnIndex("idShop");
                int nameIndex = cursor.getColumnIndex("name");
                int imageIndex = cursor.getColumnIndex("image");

                // Check if column indices are valid
                if (idCategoriesIndex >= 0 && idShopIndex >= 0 && nameIndex >= 0 && imageIndex >= 0) {
                    int idCategoriesValue = cursor.getInt(idCategoriesIndex);
                    int idShopValue = cursor.getInt(idShopIndex);
                    String name = cursor.getString(nameIndex);
                    byte[] imageBytes = cursor.getBlob(imageIndex);

                    category = new Categories();
                    category.setIdCategories(idCategoriesValue);
                    category.setIdShop(idShopValue);
                    category.setName(name);
                    category.setImage(imageBytes);
                }
            }
            cursor.close();
        }
        db.close();

        return category;
    }


}
