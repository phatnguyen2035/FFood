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
import model.OrderDetails;

public class OrderDetailsDAO {
    private DbHelper dbHelper;

    public OrderDetailsDAO(Context context) {
        dbHelper = new DbHelper(context);
    }



    public long addOrderDetails(int idShop,int idOrder, int idProduct, int quantity, double price, double totalPrice, byte[] image, String name,int  status) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM  OrderDetails  WHERE idProduct = ? and idOrder =?", new String[]{String.valueOf(idProduct),String.valueOf(idOrder)});
        if (cursor.getCount() > 0) {
            cursor.close();
            db.close();
            return 0;
        }

        // Nếu sản phẩm chưa có, thêm vào bảng
        ContentValues contentValues = new ContentValues();
        contentValues.put("idShop", idShop);
        contentValues.put("idOrder", idOrder);
        contentValues.put("idProduct", idProduct);
        contentValues.put("quantity", quantity);
        contentValues.put("price", price);
        contentValues.put("totalPrice", totalPrice);
        contentValues.put("image", image);
        contentValues.put("name", name);
        contentValues.put("status", status);

        long check = db.insert("OrderDetails", null, contentValues);
        db.close();

        return (check == -1) ? -1 : 1; // Trả về -1 nếu thêm không thành công, ngược lại trả về 1
    }


    public List<OrderDetails> getOrderDetailsIdUserStatus(int idOrder, int status) {
        List<OrderDetails> orderDetailsList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Correct SQL query with parameterized arguments
        String query = "SELECT * FROM OrderDetails WHERE  idOrder = ? AND status = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(idOrder),String.valueOf(status)});

        if (cursor.moveToFirst()) {
            do {
                OrderDetails orderDetails = new OrderDetails();

                // Use getColumnIndexOrThrow to ensure the column exists
                orderDetails.setIdOrderDetails(cursor.getInt(cursor.getColumnIndexOrThrow("idOrderDetails")));
                orderDetails.setIdShop(cursor.getInt(cursor.getColumnIndexOrThrow("idShop")));
                orderDetails.setIdOrder(cursor.getInt(cursor.getColumnIndexOrThrow("idOrder")));
                orderDetails.setIdProduct(cursor.getInt(cursor.getColumnIndexOrThrow("idProduct")));
                orderDetails.setQuantity(cursor.getInt(cursor.getColumnIndexOrThrow("quantity")));
                orderDetails.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow("price")));
                orderDetails.setTotalPrice(cursor.getDouble(cursor.getColumnIndexOrThrow("totalPrice")));
                orderDetails.setImage(cursor.getBlob(cursor.getColumnIndexOrThrow("image")));
                orderDetails.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                orderDetails.setStatus(cursor.getInt(cursor.getColumnIndexOrThrow("status")));

                orderDetailsList.add(orderDetails);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return orderDetailsList;
    }

    public List<OrderDetails> getOrderDetailsListStatus( int status) {
        List<OrderDetails> orderDetailsList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Correct SQL query with parameterized arguments
        String query = "SELECT * FROM OrderDetails WHERE status = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(status)});

        if (cursor.moveToFirst()) {
            do {
                OrderDetails orderDetails = new OrderDetails();

                // Use getColumnIndexOrThrow to ensure the column exists
                orderDetails.setIdOrderDetails(cursor.getInt(cursor.getColumnIndexOrThrow("idOrderDetails")));
                orderDetails.setIdShop(cursor.getInt(cursor.getColumnIndexOrThrow("idShop")));
                orderDetails.setIdOrder(cursor.getInt(cursor.getColumnIndexOrThrow("idOrder")));
                orderDetails.setIdProduct(cursor.getInt(cursor.getColumnIndexOrThrow("idProduct")));
                orderDetails.setQuantity(cursor.getInt(cursor.getColumnIndexOrThrow("quantity")));
                orderDetails.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow("price")));
                orderDetails.setTotalPrice(cursor.getDouble(cursor.getColumnIndexOrThrow("totalPrice")));
                orderDetails.setImage(cursor.getBlob(cursor.getColumnIndexOrThrow("image")));
                orderDetails.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                orderDetails.setStatus(cursor.getInt(cursor.getColumnIndexOrThrow("status")));

                orderDetailsList.add(orderDetails);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return orderDetailsList;
    }

    public List<OrderDetails> getOrderDetailsIdOrder(int idOrder) {
        List<OrderDetails> orderDetailsList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Correct SQL query with parameterized arguments
        String query = "SELECT * FROM OrderDetails WHERE  idOrder = ? AND status = 1";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(idOrder)});

        if (cursor.moveToFirst()) {
            do {
                OrderDetails orderDetails = new OrderDetails();

                // Use getColumnIndexOrThrow to ensure the column exists
                orderDetails.setIdOrderDetails(cursor.getInt(cursor.getColumnIndexOrThrow("idOrderDetails")));
                orderDetails.setIdShop(cursor.getInt(cursor.getColumnIndexOrThrow("idShop")));
                orderDetails.setIdOrder(cursor.getInt(cursor.getColumnIndexOrThrow("idOrder")));
                orderDetails.setIdProduct(cursor.getInt(cursor.getColumnIndexOrThrow("idProduct")));
                orderDetails.setQuantity(cursor.getInt(cursor.getColumnIndexOrThrow("quantity")));
                orderDetails.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow("price")));
                orderDetails.setTotalPrice(cursor.getDouble(cursor.getColumnIndexOrThrow("totalPrice")));
                orderDetails.setImage(cursor.getBlob(cursor.getColumnIndexOrThrow("image")));
                orderDetails.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                orderDetails.setStatus(cursor.getInt(cursor.getColumnIndexOrThrow("status")));

                orderDetailsList.add(orderDetails);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return orderDetailsList;
    }



    public boolean deleteOrderDetails(int idOrderDetails) {
        try (SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase()) {

            int rowsDeleted = sqLiteDatabase.delete("OrderDetails", "idOrderDetails = ?", new String[]{String.valueOf(idOrderDetails)});

            return rowsDeleted > 0;
        } catch (SQLiteException e) {
            Log.e("OrderDetailsDAO", "Failed to delete order detail: " + e.getMessage());
            return false;
        }
    }

    public boolean updateOrderDetails( OrderDetails orderDetails) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String tableName = "OrderDetails";
        ContentValues contentValues = new ContentValues();
        contentValues.put("quantity", orderDetails.getQuantity());
        contentValues.put("totalPrice", orderDetails.getTotalPrice());

        int rows = db.update(tableName, contentValues, "idOrderDetails = ?", new String[]{String.valueOf(orderDetails.getIdOrderDetails())});
        db.close();
        return rows > 0;
    }

    public boolean updateOrderDetailsToOrder( OrderDetails orderDetails) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String tableName = "OrderDetails";
        ContentValues contentValues = new ContentValues();
        contentValues.put("idOrder", orderDetails.getIdOrder());
        contentValues.put("status", orderDetails.getStatus());

        int rows = db.update(tableName, contentValues, "idOrderDetails = ?", new String[]{String.valueOf(orderDetails.getIdOrderDetails())});
        db.close();
        return rows > 0;
    }

    public boolean updateOrderDetailsSold( OrderDetails orderDetails) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String tableName = "OrderDetails";
        ContentValues contentValues = new ContentValues();
        contentValues.put("sold", orderDetails.getIdOrder());

        int rows = db.update(tableName, contentValues, "idOrderDetails = ?", new String[]{String.valueOf(orderDetails.getIdOrderDetails())});
        db.close();
        return rows > 0;
    }

    public OrderDetails getOrderDetailsByIdProduct(int idProduct) {
        OrderDetails orderDetails = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Correct SQL query with parameterized arguments
        String query = "SELECT * FROM OrderDetails WHERE idProduct = ? AND status = 1";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(idProduct)});

        if (cursor.moveToFirst()) {
            orderDetails = new OrderDetails();

            // Use getColumnIndexOrThrow to ensure the column exists
            orderDetails.setIdOrderDetails(cursor.getInt(cursor.getColumnIndexOrThrow("idOrderDetails")));
            orderDetails.setIdShop(cursor.getInt(cursor.getColumnIndexOrThrow("idShop")));
            orderDetails.setIdOrder(cursor.getInt(cursor.getColumnIndexOrThrow("idOrder")));
            orderDetails.setIdProduct(cursor.getInt(cursor.getColumnIndexOrThrow("idProduct")));
            orderDetails.setQuantity(cursor.getInt(cursor.getColumnIndexOrThrow("quantity")));
            orderDetails.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow("price")));
            orderDetails.setTotalPrice(cursor.getDouble(cursor.getColumnIndexOrThrow("totalPrice")));
            orderDetails.setImage(cursor.getBlob(cursor.getColumnIndexOrThrow("image")));
            orderDetails.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
            orderDetails.setStatus(cursor.getInt(cursor.getColumnIndexOrThrow("status")));
        }

        cursor.close();
        db.close();
        return orderDetails;
    }

}
