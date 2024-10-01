package dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import data.base.DbHelper;
import model.Order;

public class OrderDAO {
    private DbHelper dbHelper;

    public OrderDAO(Context context) {
        dbHelper = new DbHelper(context);
    }

    public long addOrder(int idShop, int idUser, int quantity, Double totalPrice, String date, String note, String name, long phone, String address, int status) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id = -1;

        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("idShop", idShop);
            contentValues.put("idUser", idUser);
            contentValues.put("quantity", quantity);
            contentValues.put("totalPrice", totalPrice);
            contentValues.put("date", date);
            contentValues.put("note", note);
            contentValues.put("name", name);
            contentValues.put("phone", phone);
            contentValues.put("address", address);
            contentValues.put("status", status);

            id = db.insert("OrderTable", null, contentValues);
            if (id == -1) {
                Log.e("Database", "Failed to insert order");
            }
        } catch (Exception e) {
            Log.e("Database", "Error while trying to add order to database", e);
        } finally {
            db.close();
        }

        return id;
    }
    public List<Order> getOrderByIdUser(int idUser) {
        List<Order> orderList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        try (Cursor cursor = db.rawQuery(
                "SELECT idOrder, idShop, idUser, quantity, totalPrice, date, note, name, phone, address, status " +
                        "FROM OrderTable " +
                        "WHERE idShop = ?" +
                        "ORDER BY date DESC",
                new String[]{String.valueOf(idUser)})) {
            while (cursor.moveToNext()) {
                try {
                    Order order = new Order(
                            cursor.getInt(cursor.getColumnIndexOrThrow("idOrder")),
                            cursor.getInt(cursor.getColumnIndexOrThrow("idShop")),
                            cursor.getInt(cursor.getColumnIndexOrThrow("idUser")),
                            cursor.getInt(cursor.getColumnIndexOrThrow("quantity")),
                            cursor.getDouble(cursor.getColumnIndexOrThrow("totalPrice")),
                            cursor.getString(cursor.getColumnIndexOrThrow("date")),
                            cursor.getString(cursor.getColumnIndexOrThrow("note")),
                            cursor.getString(cursor.getColumnIndexOrThrow("name")),
                            cursor.getLong(cursor.getColumnIndexOrThrow("phone")),
                            cursor.getString(cursor.getColumnIndexOrThrow("address")),
                            cursor.getInt(cursor.getColumnIndexOrThrow("status"))
                    );
                    orderList.add(order);
                } catch (Exception e) {
                    Log.e("OrderDAO", "Error parsing order from cursor: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            Log.e("OrderDAO", "Error while fetching orders: " + e.getMessage());
        } finally {
            db.close();
        }

        return orderList;
    }

    public List<Order> getOrderByIdUserStatus(int idUser, int status) {
        List<Order> orderList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        try (Cursor cursor = db.rawQuery("SELECT idOrder, idShop, idUser, quantity, totalPrice, date, note, name, phone, address, status " +
                "FROM OrderTable " +
                "WHERE idUser = ? AND status = ? " +
                "ORDER BY date DESC", new String[]{String.valueOf(idUser), String.valueOf(status)})) {
            while (cursor.moveToNext()) {
                try {
                    Order order = new Order(
                            cursor.getInt(cursor.getColumnIndexOrThrow("idOrder")),
                            cursor.getInt(cursor.getColumnIndexOrThrow("idShop")),
                            cursor.getInt(cursor.getColumnIndexOrThrow("idUser")),
                            cursor.getInt(cursor.getColumnIndexOrThrow("quantity")),
                            cursor.getDouble(cursor.getColumnIndexOrThrow("totalPrice")),
                            cursor.getString(cursor.getColumnIndexOrThrow("date")),
                            cursor.getString(cursor.getColumnIndexOrThrow("note")),
                            cursor.getString(cursor.getColumnIndexOrThrow("name")),
                            cursor.getLong(cursor.getColumnIndexOrThrow("phone")),
                            cursor.getString(cursor.getColumnIndexOrThrow("address")),
                            cursor.getInt(cursor.getColumnIndexOrThrow("status"))
                    );
                    orderList.add(order);
                } catch (Exception e) {
                    Log.e("OrderDAO", "Error parsing order from cursor: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            Log.e("OrderDAO", "Error while fetching orders: " + e.getMessage());
        } finally {
            db.close();
        }

        return orderList;
    }

    public Order getOrderByIdOrder(int idOrder) {
        Order order = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        try (Cursor cursor = db.rawQuery(
                "SELECT idOrder, idShop, idUser, quantity, totalPrice, date, note, name, phone, address, status " +
                        "FROM OrderTable " +
                        "WHERE idOrder = ?",
                new String[]{String.valueOf(idOrder)})) {

            if (cursor.moveToFirst()) {
                try {
                    order = new Order(
                            cursor.getInt(cursor.getColumnIndexOrThrow("idOrder")),
                            cursor.getInt(cursor.getColumnIndexOrThrow("idShop")),
                            cursor.getInt(cursor.getColumnIndexOrThrow("idUser")),
                            cursor.getInt(cursor.getColumnIndexOrThrow("quantity")),
                            cursor.getDouble(cursor.getColumnIndexOrThrow("totalPrice")),
                            cursor.getString(cursor.getColumnIndexOrThrow("date")),
                            cursor.getString(cursor.getColumnIndexOrThrow("note")),
                            cursor.getString(cursor.getColumnIndexOrThrow("name")),
                            cursor.getLong(cursor.getColumnIndexOrThrow("phone")),
                            cursor.getString(cursor.getColumnIndexOrThrow("address")),
                            cursor.getInt(cursor.getColumnIndexOrThrow("status"))
                    );
                } catch (Exception e) {
                    Log.e("OrderDAO", "Error parsing order from cursor: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            Log.e("OrderDAO", "Error while fetching order: " + e.getMessage());
        } finally {
            db.close();
        }

        return order;
    }

    public List<Order> getOrderByStatus( int status) {
        List<Order> orderList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        try (Cursor cursor = db.rawQuery(
                "SELECT idOrder, idShop, idUser, quantity, totalPrice, date, note, name, phone, address, status " +
                        "FROM OrderTable " +
                        "WHERE status = ? " +
                        "ORDER BY date DESC",
                new String[]{String.valueOf(status)})) {
            while (cursor.moveToNext()) {
                try {
                    Order order = new Order(
                            cursor.getInt(cursor.getColumnIndexOrThrow("idOrder")),
                            cursor.getInt(cursor.getColumnIndexOrThrow("idShop")),
                            cursor.getInt(cursor.getColumnIndexOrThrow("idUser")),
                            cursor.getInt(cursor.getColumnIndexOrThrow("quantity")),
                            cursor.getDouble(cursor.getColumnIndexOrThrow("totalPrice")),
                            cursor.getString(cursor.getColumnIndexOrThrow("date")),
                            cursor.getString(cursor.getColumnIndexOrThrow("note")),
                            cursor.getString(cursor.getColumnIndexOrThrow("name")),
                            cursor.getLong(cursor.getColumnIndexOrThrow("phone")),
                            cursor.getString(cursor.getColumnIndexOrThrow("address")),
                            cursor.getInt(cursor.getColumnIndexOrThrow("status"))
                    );
                    orderList.add(order);
                } catch (Exception e) {
                    Log.e("OrderDAO", "Error parsing order from cursor: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            Log.e("OrderDAO", "Error while fetching orders: " + e.getMessage());
        } finally {
            db.close();
        }

        return orderList;
    }
    public List<Order> getOrderByStatus2And3() {
        List<Order> orderList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        try (Cursor cursor = db.rawQuery(
                "SELECT idOrder, idShop, idUser, quantity, totalPrice, date, note, name, phone, address, status " +
                        "FROM OrderTable " +
                        "WHERE status = 2 OR status = 3 " +  // Sửa đổi câu lệnh WHERE
                        "ORDER BY date DESC", null)) { // Chèn tham số null cho rawQuery

            while (cursor.moveToNext()) {
                try {
                    Order order = new Order(
                            cursor.getInt(cursor.getColumnIndexOrThrow("idOrder")),
                            cursor.getInt(cursor.getColumnIndexOrThrow("idShop")),
                            cursor.getInt(cursor.getColumnIndexOrThrow("idUser")),
                            cursor.getInt(cursor.getColumnIndexOrThrow("quantity")),
                            cursor.getDouble(cursor.getColumnIndexOrThrow("totalPrice")),
                            cursor.getString(cursor.getColumnIndexOrThrow("date")),
                            cursor.getString(cursor.getColumnIndexOrThrow("note")),
                            cursor.getString(cursor.getColumnIndexOrThrow("name")),
                            cursor.getLong(cursor.getColumnIndexOrThrow("phone")),
                            cursor.getString(cursor.getColumnIndexOrThrow("address")),
                            cursor.getInt(cursor.getColumnIndexOrThrow("status"))
                    );
                    orderList.add(order);
                } catch (Exception e) {
                    Log.e("OrderDAO", "Error parsing order from cursor: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            Log.e("OrderDAO", "Error while fetching orders: " + e.getMessage());
        } finally {
            db.close();
        }

        return orderList;
    }


    public List<Order> getOrderByIdShopStatus(int idShop, int status) {
        List<Order> orderList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        try (Cursor cursor = db.rawQuery(
                "SELECT idOrder, idShop, idUser, quantity, totalPrice, date, note, name, phone, address, status " +
                        "FROM OrderTable " +
                        "WHERE idShop = ? AND status = ? " +
                        "ORDER BY date DESC",
                new String[]{String.valueOf(idShop), String.valueOf(status)})) {
            while (cursor.moveToNext()) {
                try {
                    Order order = new Order(
                            cursor.getInt(cursor.getColumnIndexOrThrow("idOrder")),
                            cursor.getInt(cursor.getColumnIndexOrThrow("idShop")),
                            cursor.getInt(cursor.getColumnIndexOrThrow("idUser")),
                            cursor.getInt(cursor.getColumnIndexOrThrow("quantity")),
                            cursor.getDouble(cursor.getColumnIndexOrThrow("totalPrice")),
                            cursor.getString(cursor.getColumnIndexOrThrow("date")),
                            cursor.getString(cursor.getColumnIndexOrThrow("note")),
                            cursor.getString(cursor.getColumnIndexOrThrow("name")),
                            cursor.getLong(cursor.getColumnIndexOrThrow("phone")),
                            cursor.getString(cursor.getColumnIndexOrThrow("address")),
                            cursor.getInt(cursor.getColumnIndexOrThrow("status"))
                    );
                    orderList.add(order);
                } catch (Exception e) {
                    Log.e("OrderDAO", "Error parsing order from cursor: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            Log.e("OrderDAO", "Error while fetching orders: " + e.getMessage());
        } finally {
            db.close();
        }

        return orderList;
    }


    public List<Order> getOrderAllList() {
        List<Order> orderList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        try (Cursor cursor = db.rawQuery("SELECT idOrder, idShop, idUser, quantity, totalPrice, date, note, name, phone, address, status " +
                                                "FROM OrderTable " +
                                                "ORDER BY date DESC", null)) {
            while (cursor.moveToNext()) {
                Order order = new Order(
                        cursor.getInt(cursor.getColumnIndexOrThrow("idOrder")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("idShop")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("idUser")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("quantity")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("totalPrice")),
                        cursor.getString(cursor.getColumnIndexOrThrow("date")),
                        cursor.getString(cursor.getColumnIndexOrThrow("note")),
                        cursor.getString(cursor.getColumnIndexOrThrow("name")),
                        cursor.getLong(cursor.getColumnIndexOrThrow("phone")),
                        cursor.getString(cursor.getColumnIndexOrThrow("address")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("status"))
                );
                orderList.add(order);
            }
        } catch (Exception e) {
            Log.e("OrderDAO", "Error while fetching all orders: " + e.getMessage());
        } finally {
            db.close();
        }

        return orderList;
    }

    public boolean updateOrder(Order order) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        boolean result = false;

        try {
            ContentValues values = new ContentValues();
            values.put("note", order.getNote());
            values.put("status", order.getStatus());

            int rows = sqLiteDatabase.update("OrderTable", values, "idOrder=?", new String[]{String.valueOf(order.getIdOrder())});
            result = rows > 0;
        } catch (Exception e) {
            Log.e("OrderDAO", "Error while updating order: " + e.getMessage());
        } finally {
            sqLiteDatabase.close();
        }

        return result;
    }
}