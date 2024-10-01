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
import model.Product;

public class ProductDAO {
    DbHelper dbHelper;

    public ProductDAO(Context context) {
        this.dbHelper = new DbHelper(context);
    }


    public int addProduct(int idCategories, int idShop, String name, byte[] image, double price, String note, int status, int sold) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        Cursor cursor = db.rawQuery("SELECT * FROM Product WHERE name = ?", new String[]{name});
//        if (cursor.getCount() > 0) {
//            cursor.close();
//            db.close();
//            return 0;
//        }

        // Nếu danh mục chưa tồn tại, thêm mới vào cơ sở dữ liệu
        ContentValues contentValues = new ContentValues();
        contentValues.put("idCategories", idCategories);
        contentValues.put("idShop", idShop);
        contentValues.put("name", name);
        contentValues.put("image", image);
        contentValues.put("price", price);
        contentValues.put("note", note);
        contentValues.put("status", status);
        contentValues.put("sold", sold);


        long check = db.insert("Product", null, contentValues);

        db.close();

        return (check == -1) ? -1 : 1; // Trả về -1 nếu thêm mới không thành công, ngược lại trả về 1
    }

    public List<Product> getProductsByIdShop(int idShop) {
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT idProduct, idCategories, idShop, name, image, price, note, status, sold FROM Product WHERE idShop = ?", new String[]{String.valueOf(idShop)});
        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int idProductIndex = cursor.getColumnIndex("idProduct");
                    int idCategoriesIndex = cursor.getColumnIndex("idCategories");
                    int idShopIndex = cursor.getColumnIndex("idShop");
                    int nameIndex = cursor.getColumnIndex("name");
                    int imageIndex = cursor.getColumnIndex("image");
                    int priceIndex = cursor.getColumnIndex("price");
                    int noteIndex = cursor.getColumnIndex("note");
                    int statusIndex = cursor.getColumnIndex("status");
                    int soldIndex = cursor.getColumnIndex("sold");

                    if (idProductIndex >= 0 && idCategoriesIndex >= 0 && idShopIndex >= 0 && nameIndex >= 0 && imageIndex >= 0 && priceIndex >= 0 && noteIndex >= 0 && statusIndex >= 0 && soldIndex >= 0) {
                        int idProduct = cursor.getInt(idProductIndex);
                        int idCategories = cursor.getInt(idCategoriesIndex);
                        int idShopValue = cursor.getInt(idShopIndex);
                        String name = cursor.getString(nameIndex);
                        byte[] imageBytes = cursor.getBlob(imageIndex);
                        int price = cursor.getInt(priceIndex);
                        String note = cursor.getString(noteIndex);
                        int status = cursor.getInt(statusIndex);
                        int sold = cursor.getInt(soldIndex);

                        Product product = new Product(idProduct, idCategories, idShopValue, name, imageBytes, price, note, status, sold);
                        productList.add(product);
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("ProductAdapter", "Error while fetching products: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return productList;
    }

    public boolean deleteProduct(int idProduct, int idShop) {
        try (SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase()) {
            int check = sqLiteDatabase.delete("Product", "idProduct=? AND idShop=?", new String[]{String.valueOf(idProduct), String.valueOf(idShop)});
            if (check <= 0) {
                Log.e("ProductDAO", "Failed to delete category with idProduct: " + idProduct + " and idShop: " + idShop);
                return false;
            }
            return true;
        } catch (SQLiteException e) {
            Log.e("ProductDAO", "SQLiteException: " + e.getMessage());
            return false;
        }
    }

    public boolean upStatust(Product product) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("status", product.getStatus());

        int check = sqLiteDatabase.update("Product", values, "idProduct=?", new String[]{String.valueOf(product.getIdProduct())});
        if (check <= 0) return false;
        return true;
    }

    public boolean upProduct(Product product) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", product.getName());
        values.put("price", product.getPrice());
        values.put("note", product.getNote());
        values.put("status", product.getStatus());

        int check = sqLiteDatabase.update("Product", values, "idProduct=?", new String[]{String.valueOf(product.getIdProduct())});
        if (check <= 0) return false;
        return true;
    }

    public boolean upProductSold(Product product) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("sold", product.getSold());

        int check = sqLiteDatabase.update("Product", values, "idProduct=?", new String[]{String.valueOf(product.getIdProduct())});
        if (check <= 0) return false;
        return true;
    }

    public boolean upSold(Product product) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("sold", product.getSold());

        int check = sqLiteDatabase.update("Product", values, "idProduct=?", new String[]{String.valueOf(product.getIdProduct())});
        if (check <= 0) return false;
        return true;
    }

    public List<Product> getProductsListEat() {
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT idProduct, idCategories, idShop, name, image, price, note, status, sold FROM Product where status == 1 AND idCategories IN (1,2,3,4,5) ", null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int idProductIndex = cursor.getColumnIndex("idProduct");
                    int idCategoriesIndex = cursor.getColumnIndex("idCategories");
                    int idShopIndex = cursor.getColumnIndex("idShop");
                    int nameIndex = cursor.getColumnIndex("name");
                    int imageIndex = cursor.getColumnIndex("image");
                    int priceIndex = cursor.getColumnIndex("price");
                    int noteIndex = cursor.getColumnIndex("note");
                    int statusIndex = cursor.getColumnIndex("status");
                    int soldIndex = cursor.getColumnIndex("sold");

                    if (idProductIndex >= 0 && idCategoriesIndex >= 0 && idShopIndex >= 0 && nameIndex >= 0 && imageIndex >= 0 && priceIndex >= 0 && noteIndex >= 0 && statusIndex >= 0 && soldIndex >= 0) {
                        int idProduct = cursor.getInt(idProductIndex);
                        int idCategories = cursor.getInt(idCategoriesIndex);
                        int idShopValue = cursor.getInt(idShopIndex);
                        String name = cursor.getString(nameIndex);
                        byte[] imageBytes = cursor.getBlob(imageIndex);
                        int price = cursor.getInt(priceIndex);
                        String note = cursor.getString(noteIndex);
                        int status = cursor.getInt(statusIndex);
                        int sold = cursor.getInt(soldIndex);

                        Product product = new Product(idProduct, idCategories, idShopValue, name, imageBytes, price, note, status, sold);
                        productList.add(product);
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("ProductAdapter", "Error while fetching products: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return productList;
    }

    public List<Product> getProductsListDrinks() {
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT idProduct, idCategories, idShop, name, image, price, note, status, sold FROM Product where status == 1 AND idCategories IN (6,7,8,9,10) ", null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int idProductIndex = cursor.getColumnIndex("idProduct");
                    int idCategoriesIndex = cursor.getColumnIndex("idCategories");
                    int idShopIndex = cursor.getColumnIndex("idShop");
                    int nameIndex = cursor.getColumnIndex("name");
                    int imageIndex = cursor.getColumnIndex("image");
                    int priceIndex = cursor.getColumnIndex("price");
                    int noteIndex = cursor.getColumnIndex("note");
                    int statusIndex = cursor.getColumnIndex("status");
                    int soldIndex = cursor.getColumnIndex("sold");

                    if (idProductIndex >= 0 && idCategoriesIndex >= 0 && idShopIndex >= 0 && nameIndex >= 0 && imageIndex >= 0 && priceIndex >= 0 && noteIndex >= 0 && statusIndex >= 0 && soldIndex >= 0) {
                        int idProduct = cursor.getInt(idProductIndex);
                        int idCategories = cursor.getInt(idCategoriesIndex);
                        int idShopValue = cursor.getInt(idShopIndex);
                        String name = cursor.getString(nameIndex);
                        byte[] imageBytes = cursor.getBlob(imageIndex);
                        int price = cursor.getInt(priceIndex);
                        String note = cursor.getString(noteIndex);
                        int status = cursor.getInt(statusIndex);
                        int sold = cursor.getInt(soldIndex);

                        Product product = new Product(idProduct, idCategories, idShopValue, name, imageBytes, price, note, status, sold);
                        productList.add(product);
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("ProductAdapter", "Error while fetching products: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return productList;
    }

    public List<Product> getProductsListEatShop(int idShop) {
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT idProduct, idCategories, idShop, name, image, price, note, status, sold " + "FROM Product " + "WHERE status = 1 AND idCategories IN (1, 2, 3, 4, 5) AND idShop = ?";
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(query, new String[]{String.valueOf(idShop)});
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int idProduct = cursor.getInt(cursor.getColumnIndexOrThrow("idProduct"));
                    int idCategories = cursor.getInt(cursor.getColumnIndexOrThrow("idCategories"));
                    int idShopValue = cursor.getInt(cursor.getColumnIndexOrThrow("idShop"));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                    byte[] imageBytes = cursor.getBlob(cursor.getColumnIndexOrThrow("image"));
                    int price = cursor.getInt(cursor.getColumnIndexOrThrow("price"));
                    String note = cursor.getString(cursor.getColumnIndexOrThrow("note"));
                    int status = cursor.getInt(cursor.getColumnIndexOrThrow("status"));
                    int sold = cursor.getInt(cursor.getColumnIndexOrThrow("sold"));

                    Product product = new Product(idProduct, idCategories, idShopValue, name, imageBytes, price, note, status, sold);
                    productList.add(product);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("ProductDAO", "Error while fetching products: " + e.getMessage(), e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return productList;
    }

    public List<Product> getProductsListDrinksShop(int idShop) {
        // Tương tự như phương thức trên, thay đổi idCategories và các điều kiện nếu cần
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT idProduct, idCategories, idShop, name, image, price, note, status, sold " + "FROM Product " + "WHERE status = 1 AND idCategories IN (6, 7, 8, 9, 10) AND idShop = ?";
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(query, new String[]{String.valueOf(idShop)});
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int idProduct = cursor.getInt(cursor.getColumnIndexOrThrow("idProduct"));
                    int idCategories = cursor.getInt(cursor.getColumnIndexOrThrow("idCategories"));
                    int idShopValue = cursor.getInt(cursor.getColumnIndexOrThrow("idShop"));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                    byte[] imageBytes = cursor.getBlob(cursor.getColumnIndexOrThrow("image"));
                    int price = cursor.getInt(cursor.getColumnIndexOrThrow("price"));
                    String note = cursor.getString(cursor.getColumnIndexOrThrow("note"));
                    int status = cursor.getInt(cursor.getColumnIndexOrThrow("status"));
                    int sold = cursor.getInt(cursor.getColumnIndexOrThrow("sold"));

                    Product product = new Product(idProduct, idCategories, idShopValue, name, imageBytes, price, note, status, sold);
                    productList.add(product);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("ProductDAO", "Error while fetching products: " + e.getMessage(), e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return productList;
    }

    public List<Product> getProductsListAll() {
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT idProduct, idCategories, idShop, name, image, price, note, status,sold FROM Product where status = 1", null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int idProductIndex = cursor.getColumnIndex("idProduct");
                    int idCategoriesIndex = cursor.getColumnIndex("idCategories");
                    int idShopIndex = cursor.getColumnIndex("idShop");
                    int nameIndex = cursor.getColumnIndex("name");
                    int imageIndex = cursor.getColumnIndex("image");
                    int priceIndex = cursor.getColumnIndex("price");
                    int noteIndex = cursor.getColumnIndex("note");
                    int statusIndex = cursor.getColumnIndex("status");
                    int soldIndex = cursor.getColumnIndex("sold");

                    if (idProductIndex >= 0 && idCategoriesIndex >= 0 && idShopIndex >= 0 && nameIndex >= 0 && imageIndex >= 0 && priceIndex >= 0 && noteIndex >= 0 && statusIndex >= 0 && soldIndex >= 0) {
                        int idProduct = cursor.getInt(idProductIndex);
                        int idCategories = cursor.getInt(idCategoriesIndex);
                        int idShopValue = cursor.getInt(idShopIndex);
                        String name = cursor.getString(nameIndex);
                        byte[] imageBytes = cursor.getBlob(imageIndex);
                        int price = cursor.getInt(priceIndex);
                        String note = cursor.getString(noteIndex);
                        int status = cursor.getInt(statusIndex);
                        int sold = cursor.getInt(soldIndex);

                        Product product = new Product(idProduct, idCategories, idShopValue, name, imageBytes, price, note, status, sold);
                        productList.add(product);
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("ProductAdapter", "Error while fetching products: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return productList;
    }

    public List<Product> getProductsListByIdCategories(int idCategoriess) {
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT idProduct, idCategories, idShop, name, image, price, note, status,sold " +
                "FROM Product  " +
                "where status = 1 and idCategories = ?", new String[]{String.valueOf(idCategoriess)});
        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int idProductIndex = cursor.getColumnIndex("idProduct");
                    int idCategoriesIndex = cursor.getColumnIndex("idCategories");
                    int idShopIndex = cursor.getColumnIndex("idShop");
                    int nameIndex = cursor.getColumnIndex("name");
                    int imageIndex = cursor.getColumnIndex("image");
                    int priceIndex = cursor.getColumnIndex("price");
                    int noteIndex = cursor.getColumnIndex("note");
                    int statusIndex = cursor.getColumnIndex("status");
                    int soldIndex = cursor.getColumnIndex("sold");

                    if (idProductIndex >= 0 && idCategoriesIndex >= 0 && idShopIndex >= 0 && nameIndex >= 0 && imageIndex >= 0 && priceIndex >= 0 && noteIndex >= 0 && statusIndex >= 0 && soldIndex >= 0) {
                        int idProduct = cursor.getInt(idProductIndex);
                        int idCategories = cursor.getInt(idCategoriesIndex);
                        int idShopValue = cursor.getInt(idShopIndex);
                        String name = cursor.getString(nameIndex);
                        byte[] imageBytes = cursor.getBlob(imageIndex);
                        int price = cursor.getInt(priceIndex);
                        String note = cursor.getString(noteIndex);
                        int status = cursor.getInt(statusIndex);
                        int sold = cursor.getInt(soldIndex);

                        Product product = new Product(idProduct, idCategories, idShopValue, name, imageBytes, price, note, status, sold);
                        productList.add(product);
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("ProductAdapter", "Error while fetching products: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return productList;
    }

    public List<Product> getProductsListPriceMax() {
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT idProduct, idCategories, idShop, name, image, price, note, status, sold " +
                    "FROM Product WHERE status = 1 " +
                    "ORDER BY price DESC", null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int idProduct = cursor.getInt(cursor.getColumnIndexOrThrow("idProduct"));
                    int idCategories = cursor.getInt(cursor.getColumnIndexOrThrow("idCategories"));
                    int idShop = cursor.getInt(cursor.getColumnIndexOrThrow("idShop"));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                    byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow("image"));
                    int price = cursor.getInt(cursor.getColumnIndexOrThrow("price"));
                    String note = cursor.getString(cursor.getColumnIndexOrThrow("note"));
                    int status = cursor.getInt(cursor.getColumnIndexOrThrow("status"));
                    int sold = cursor.getInt(cursor.getColumnIndexOrThrow("sold"));

                    Product product = new Product(idProduct, idCategories, idShop, name, image, price, note, status, sold);
                    productList.add(product);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("ProductAdapter", "Error while fetching products: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return productList;
    }

    public List<Product> getProductsListPriceMin() {
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT idProduct, idCategories, idShop, name, image, price, note, status, sold " +
                    "FROM Product WHERE status = 1 " +
                    "ORDER BY price ASC", null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int idProduct = cursor.getInt(cursor.getColumnIndexOrThrow("idProduct"));
                    int idCategories = cursor.getInt(cursor.getColumnIndexOrThrow("idCategories"));
                    int idShop = cursor.getInt(cursor.getColumnIndexOrThrow("idShop"));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                    byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow("image"));
                    int price = cursor.getInt(cursor.getColumnIndexOrThrow("price"));
                    String note = cursor.getString(cursor.getColumnIndexOrThrow("note"));
                    int status = cursor.getInt(cursor.getColumnIndexOrThrow("status"));
                    int sold = cursor.getInt(cursor.getColumnIndexOrThrow("sold"));

                    Product product = new Product(idProduct, idCategories, idShop, name, image, price, note, status, sold);
                    productList.add(product);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("ProductAdapter", "Error while fetching products: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return productList;
    }

    public List<Product> getProductsListSold() {
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT idProduct, idCategories, idShop, name, image, price, note, status, sold " +
                    "FROM Product WHERE status = 1 " +
                    "ORDER BY sold DESC", null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int idProduct = cursor.getInt(cursor.getColumnIndexOrThrow("idProduct"));
                    int idCategories = cursor.getInt(cursor.getColumnIndexOrThrow("idCategories"));
                    int idShop = cursor.getInt(cursor.getColumnIndexOrThrow("idShop"));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                    byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow("image"));
                    int price = cursor.getInt(cursor.getColumnIndexOrThrow("price"));
                    String note = cursor.getString(cursor.getColumnIndexOrThrow("note"));
                    int status = cursor.getInt(cursor.getColumnIndexOrThrow("status"));
                    int sold = cursor.getInt(cursor.getColumnIndexOrThrow("sold"));

                    Product product = new Product(idProduct, idCategories, idShop, name, image, price, note, status, sold);
                    productList.add(product);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("ProductAdapter", "Error while fetching products: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return productList;
    }


    public List<Product> getProductsListNew() {
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT idProduct, idCategories, idShop, name, image, price, note, status, sold " +
                    "FROM Product WHERE status = 1 " +
                    "ORDER BY idProduct DESC", null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int idProduct = cursor.getInt(cursor.getColumnIndexOrThrow("idProduct"));
                    int idCategories = cursor.getInt(cursor.getColumnIndexOrThrow("idCategories"));
                    int idShop = cursor.getInt(cursor.getColumnIndexOrThrow("idShop"));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                    byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow("image"));
                    int price = cursor.getInt(cursor.getColumnIndexOrThrow("price"));
                    String note = cursor.getString(cursor.getColumnIndexOrThrow("note"));
                    int status = cursor.getInt(cursor.getColumnIndexOrThrow("status"));
                    int sold = cursor.getInt(cursor.getColumnIndexOrThrow("sold"));

                    Product product = new Product(idProduct, idCategories, idShop, name, image, price, note, status, sold);
                    productList.add(product);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("ProductAdapter", "Error while fetching products: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return productList;
    }




    public List<Product> getProductsByName(String query) {
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sql = "SELECT idProduct, idCategories, idShop, name, image, price, note, status, sold FROM Product WHERE name LIKE ?";
        Cursor cursor = null;

        try {
            cursor = db.rawQuery(sql, new String[]{"%" + query + "%"});
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int idProductIndex = cursor.getColumnIndex("idProduct");
                    int idCategoriesIndex = cursor.getColumnIndex("idCategories");
                    int idShopIndex = cursor.getColumnIndex("idShop");
                    int nameIndex = cursor.getColumnIndex("name");
                    int imageIndex = cursor.getColumnIndex("image");
                    int priceIndex = cursor.getColumnIndex("price");
                    int noteIndex = cursor.getColumnIndex("note");
                    int statusIndex = cursor.getColumnIndex("status");
                    int soldIndex = cursor.getColumnIndex("sold");

                    if (idProductIndex >= 0 && idCategoriesIndex >= 0 && idShopIndex >= 0 && nameIndex >= 0 && imageIndex >= 0 && priceIndex >= 0 && noteIndex >= 0 && statusIndex >= 0 && soldIndex >= 0) {
                        int idProduct = cursor.getInt(idProductIndex);
                        int idCategories = cursor.getInt(idCategoriesIndex);
                        int idShopValue = cursor.getInt(idShopIndex);
                        String name = cursor.getString(nameIndex);
                        byte[] imageBytes = cursor.getBlob(imageIndex);
                        int price = cursor.getInt(priceIndex);
                        String note = cursor.getString(noteIndex);
                        int status = cursor.getInt(statusIndex);
                        int sold = cursor.getInt(soldIndex);

                        Product product = new Product(idProduct, idCategories, idShopValue, name, imageBytes, price, note, status, sold);
                        productList.add(product);
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("ProductDAO", "Error while fetching products: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return productList;
    }

    public Product getProductById(int idProduct) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Product product = null;
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("SELECT idProduct, idCategories, idShop, name, image, price, note, status, sold FROM Product WHERE idProduct=? AND status = 1", new String[]{String.valueOf(idProduct)});

            if (cursor != null && cursor.moveToFirst()) {
                int idProductIndex = cursor.getColumnIndex("idProduct");
                int idCategoriesIndex = cursor.getColumnIndex("idCategories");
                int idShopIndex = cursor.getColumnIndex("idShop");
                int nameIndex = cursor.getColumnIndex("name");
                int imageIndex = cursor.getColumnIndex("image");
                int priceIndex = cursor.getColumnIndex("price");
                int noteIndex = cursor.getColumnIndex("note");
                int statusIndex = cursor.getColumnIndex("status");
                int soldIndex = cursor.getColumnIndex("sold");

                if (idProductIndex >= 0 && idCategoriesIndex >= 0 && idShopIndex >= 0 && nameIndex >= 0 && imageIndex >= 0 && priceIndex >= 0 && noteIndex >= 0 && statusIndex >= 0 && soldIndex >= 0) {
                    int idCategories = cursor.getInt(idCategoriesIndex);
                    int idShopValue = cursor.getInt(idShopIndex);
                    String name = cursor.getString(nameIndex);
                    byte[] imageBytes = cursor.getBlob(imageIndex);
                    int price = cursor.getInt(priceIndex);
                    String note = cursor.getString(noteIndex);
                    int status = cursor.getInt(statusIndex);
                    int sold = cursor.getInt(soldIndex);

                    product = new Product(idProduct, idCategories, idShopValue, name, imageBytes, price, note, status, sold);
                }
            }
        } catch (Exception e) {
            Log.e("ProductAdapter", "Error while fetching product: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return product;
    }
}
