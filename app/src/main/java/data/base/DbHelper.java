package data.base;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import com.example.du_an_1.R;

import java.io.ByteArrayOutputStream;

public class DbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "DuAn1";
    private static final int DATABASE_VERSION = 1;

    // Table and Column names
    private static final String TABLE_USER = "User";
    private static final String TABLE_SHOP = "Shop";
    private static final String TABLE_CATEGORIES = "Categories";
    private static final String TABLE_PRODUCT = "Product";
    private static final String TABLE_ORDER_DETAILS = "OrderDetails";
    private static final String TABLE_ORDER = "OrderTable";

    // User table columns
    private static final String COLUMN_ID_USER = "idUser";
    private static final String COLUMN_USER = "user";
    private static final String COLUMN_PASS = "pass";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_CCCD = "cccd";
    private static final String COLUMN_ROLE = "role";
    private static final String COLUMN_ADDRESS = "address";
    private static final String COLUMN_STATUS = "status";


    // Shop table columns
    private static final String COLUMN_ID_SHOP = "idShop";
    private static final String COLUMN_NAME_SHOP = "name";
    private static final String COLUMN_ADDRESS_SHOP = "address";
    private static final String COLUMN_IMAGE_SHOP = "image";
    private static final String COLUMN_STATUS_SHOP = "status";
    private static final String COLUMN_SHOP_SOLD = "sold";


    // Categories table columns
    private static final String COLUMN_ID_CATEGORIES = "idCategories";
    private static final String COLUMN_NAME_CATEGORIES = "name";
    private static final String COLUMN_IMAGE_CATEGORIES = "image";

    // Product table columns
    private static final String COLUMN_ID_PRODUCT = "idProduct";
    private static final String COLUMN_ID_SHOP_PRODUCT = "idShop";
    private static final String COLUMN_NAME_PRODUCT = "name";
    private static final String COLUMN_IMAGE_PRODUCT = "image";
    private static final String COLUMN_PRICE_PRODUCT = "price";
    private static final String COLUMN_NOTE_PRODUCT = "note";
    private static final String COLUMN_PRODUCT_SOLD = "sold";
    private static final String COLUMN_STATUS_PRODUCT = "status";

    // OrderDetails table columns
    private static final String COLUMN_ID_ORDER_DETAILS = "idOrderDetails";
    private static final String COLUMN_ID_SHOP_DETAILS = "idShop";
    private static final String COLUMN_ID_PRODUCT_DETAILS = "idProduct";
    private static final String COLUMN_PRICE_ORDER_DETAILS = "price";
    private static final String COLUMN_TOTAL_PRICE_ORDER_DETAILS = "totalPrice";
    private static final String COLUMN_QUANTITY_ORDER_DETAILS = "quantity";
    private static final String COLUMN_NAME_ORDER_DETAILS = "name";
    private static final String COLUMN_IMAGE_ORDER_DETAILS = "image";
    private static final String COLUMN_STATUS_ORDER_DETAILS = "status";


    // Order table columns
    private static final String COLUMN_ID_ORDER = "idOrder";
    private static final String COLUMN_TOTAL_PRICE_ORDER = "totalPrice";
    private static final String COLUMN_QUANTITY = "quantity";
    private static final String COLUMN_ORDER_DATE_ORDER = "date";
    private static final String COLUMN_NOTE_ORDER = "note";
    private static final String COLUMN_NAME_USER_ORDER = "name";
    private static final String COLUMN_PHONE_USER_ORDER = "phone";
    private static final String COLUMN_ADDRESS_USER_ORDER = "address";
    private static final String COLUMN_STATUS_ORDER = "status";
    private Context context;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create User table
        db.execSQL("CREATE TABLE " + TABLE_USER + " (" +
                COLUMN_ID_USER + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USER + " TEXT, " +
                COLUMN_PASS + " TEXT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_PHONE + " LONG, " +
                COLUMN_CCCD + " LONG, " +
                COLUMN_ROLE + " INTEGER, " +
                COLUMN_ADDRESS + " TEXT," +
                COLUMN_STATUS + " INTEGER" +
                ");");


        // Create Shop table
        db.execSQL("CREATE TABLE " + TABLE_SHOP + " (" +
                COLUMN_ID_SHOP + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ID_USER + " INTEGER, " +
                COLUMN_NAME_SHOP + " TEXT, " +
                COLUMN_ADDRESS_SHOP + " TEXT, " +
                COLUMN_IMAGE_SHOP + " BLOB, " +
                COLUMN_STATUS_SHOP + " INTEGER, " +

                "FOREIGN KEY(" + COLUMN_ID_USER + ") REFERENCES " + TABLE_USER + "(" + COLUMN_ID_USER + "))");

        // Create Categories table
        db.execSQL("CREATE TABLE " + TABLE_CATEGORIES + " (" +
                COLUMN_ID_CATEGORIES + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ID_SHOP + " INTEGER, " +
                COLUMN_NAME_CATEGORIES + " TEXT, " +
                COLUMN_IMAGE_CATEGORIES + " BLOB, " +
                "FOREIGN KEY(" + COLUMN_ID_SHOP + ") REFERENCES " + TABLE_SHOP + "(" + COLUMN_ID_SHOP + "))");

        // Create Product table
        db.execSQL("CREATE TABLE " + TABLE_PRODUCT + " (" +
                COLUMN_ID_PRODUCT + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ID_CATEGORIES + " INTEGER, " +
                COLUMN_ID_SHOP + " INTEGER, " +
                COLUMN_NAME_PRODUCT + " TEXT, " +
                COLUMN_IMAGE_PRODUCT + " BLOB, " +
                COLUMN_PRICE_PRODUCT + " DOUBLE, " +
                COLUMN_NOTE_PRODUCT + " TEXT, " +
                COLUMN_PRODUCT_SOLD + " INTEGER, " +
                COLUMN_STATUS_PRODUCT + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_ID_CATEGORIES + ") REFERENCES " + TABLE_CATEGORIES + "(" + COLUMN_ID_CATEGORIES + "), " +
                "FOREIGN KEY(" + COLUMN_ID_SHOP + ") REFERENCES " + TABLE_SHOP + "(" + COLUMN_ID_SHOP + "))");

        // Create OrderDetails table
        db.execSQL("CREATE TABLE " + TABLE_ORDER_DETAILS + " (" +
                COLUMN_ID_ORDER_DETAILS + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ID_SHOP_DETAILS + " INTEGER, " +
                COLUMN_ID_ORDER + " INTEGER, " +
                COLUMN_ID_PRODUCT_DETAILS + " INTEGER, " +
                COLUMN_QUANTITY_ORDER_DETAILS + " INTEGER, " +
                COLUMN_PRICE_ORDER_DETAILS + " DOUBLE, " +
                COLUMN_TOTAL_PRICE_ORDER_DETAILS + " DOUBLE, " +
                COLUMN_IMAGE_ORDER_DETAILS + " BLOB, " +
                COLUMN_NAME_ORDER_DETAILS + " TEXT, " +
                COLUMN_STATUS_ORDER_DETAILS + " INTEGER, " +

                //    public OrderDetails(int idOrderDetails, int idShop, int idOrder, int idProduct, int quantity, double price, double totalPrice, byte[] image, String name, int status) {


                "FOREIGN KEY(" + COLUMN_ID_PRODUCT + ") REFERENCES " + TABLE_PRODUCT + "(" + COLUMN_ID_PRODUCT + "), " +
                "FOREIGN KEY(" + COLUMN_ID_ORDER + ") REFERENCES " + TABLE_ORDER + "(" + COLUMN_ID_ORDER + "))");

        // Create Order table
        db.execSQL("CREATE TABLE " + TABLE_ORDER + " (" +
                COLUMN_ID_ORDER + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ID_SHOP_PRODUCT + " INTEGER, " +
                COLUMN_ID_USER + " INTEGER, " +
                COLUMN_QUANTITY + " INTEGER, " +
                COLUMN_TOTAL_PRICE_ORDER + " DOUBLE, " +
                COLUMN_ORDER_DATE_ORDER + " Text, " +
                COLUMN_NOTE_ORDER + " TEXT, " +
                COLUMN_NAME_USER_ORDER + " TEXT, " +
                COLUMN_PHONE_USER_ORDER + " LONG, " +
                COLUMN_ADDRESS_USER_ORDER + " TEXT, " +
                COLUMN_STATUS_ORDER + " INTEGER, " +

                "FOREIGN KEY(" + COLUMN_ID_USER + ") REFERENCES " + TABLE_USER + "(" + COLUMN_ID_USER + "))");


        // Insert initial data
        db.execSQL("INSERT INTO " + TABLE_USER + " (" + COLUMN_USER + ", " + COLUMN_PASS + ", " + COLUMN_NAME + ", " + COLUMN_PHONE + ", " + COLUMN_CCCD + ", " + COLUMN_ROLE + ", " + COLUMN_ADDRESS + ", " + COLUMN_STATUS + ") VALUES " +
                "('admin', '1', 'Phong Nguyễn', '0936887373', '04420020000000', 0, 'admin',1), " +
                "('sell1', '1', 'Mỹ Huyền', '0123456789', '03745923622222', 1, 'Quận 1',1), " +
                "('sell2', '1', 'Anh Tuấn', '0978654123', '03745923622222', 1, 'Lầu 2, Toà T',1), " +
                "('sell3', '1', 'Nguyễn Dung', '0972396789', '66666666666666', 1, 'Lầu 3, Toà T',1), " +
                "('buy1', '1', 'Nguyễn Văn A', '0123456789', '33333333333333', 2, 'Lầu 8, Toà T',1), " +
                "('buy2', '1', 'Lê Đức Thọ', '0123456789', '33333333333333', 2, 'Lầu 9, Toà 10',1)");


        //insertshop
        insertShops(db);
        // Insert initial categories with image
        insertInitialCategories(db);
        //product
        insertProduct(db);


    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHOP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_DETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER);
        onCreate(db);
    }

    public SQLiteDatabase open(){
        return this.getWritableDatabase();
    }

    public void insertShops(SQLiteDatabase db) {
        // Load images from resources and resize them
        Bitmap bitmap1 = resizeBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.image_shop_1), 400, 400);
        Bitmap bitmap2 = resizeBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.image_shop_2), 400, 400);

        // Convert bitmaps to byte arrays
        byte[] image1 = getBytesFromBitmap(bitmap1, 50);
        byte[] image2 = getBytesFromBitmap(bitmap2, 50);

        // Insert data
        insertShop(db, 2, "Cơm Mẹ Nga", "Lầu 8, Toà T", image2, 1);
        insertShop(db, 3, "Trà Sữa 68", "Lầu 3, Toà T", image1, 1);

    }

    private void insertInitialCategories(SQLiteDatabase db) {
        // Load images from resources and resize them
        Bitmap bitmap1 = resizeBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.image_comtron), 400, 400);
        Bitmap bitmap2 = resizeBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.img_mytom), 400, 400);
        Bitmap bitmap3 = resizeBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.img_banhmy), 400, 400);
        Bitmap bitmap4 = resizeBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.img_doanvat), 400, 400);
        Bitmap bitmap5 = resizeBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.img_khac), 400, 400);
        Bitmap bitmap6 = resizeBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.img_trasua), 400, 400);
        Bitmap bitmap7 = resizeBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.img_caphe), 400, 400);
        Bitmap bitmap8 = resizeBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.img_nuocngot), 400, 400);
        Bitmap bitmap9 = resizeBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.img_sua), 400, 400);
        Bitmap bitmap10 = resizeBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.img_khac_1), 400, 400);


// Convert bitmaps to byte arrays


        // Convert bitmaps to byte arrays
        byte[] image1 = getBytesFromBitmap(bitmap1, 100);
        byte[] image2 = getBytesFromBitmap(bitmap2, 100);
        byte[] image3 = getBytesFromBitmap(bitmap3, 100);
        byte[] image4 = getBytesFromBitmap(bitmap4, 100);
        byte[] image5 = getBytesFromBitmap(bitmap5, 100);
        byte[] image6 = getBytesFromBitmap(bitmap6, 100);
        byte[] image7 = getBytesFromBitmap(bitmap7, 100);
        byte[] image8 = getBytesFromBitmap(bitmap8, 100);
        byte[] image9 = getBytesFromBitmap(bitmap9, 100);
        byte[] image10 = getBytesFromBitmap(bitmap10, 100);

        // Insert categories into the database
        insertCategory(db, 1, "Cơm", image1);
        insertCategory(db, 1, "Mỳ", image2);
        insertCategory(db, 1, "Bánh Mỳ", image3);
        insertCategory(db, 1, "Đồ Ăn Vặt", image4);
        insertCategory(db, 1, "Đồ Ăn Khác", image5);
        insertCategory(db, 1, "Trà Sữa", image6);
        insertCategory(db, 1, "Cà Phê", image7);
        insertCategory(db, 1, "Nước Ngọt", image8);
        insertCategory(db, 1, "Sữa", image9);
        insertCategory(db, 1, "Nước Khác", image10);
    }

    private void insertProduct(SQLiteDatabase db) {
        // Load images from resources and resize them
        Bitmap bitmap1 = resizeBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.img_comtam), 400, 400);
        Bitmap bitmap2 = resizeBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.img_mytom), 400, 400);
        Bitmap bitmap3 = resizeBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.img_banhmy), 400, 400);
        Bitmap bitmap4 = resizeBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.img_doanvat), 400, 400);
        Bitmap bitmap5 = resizeBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.img_khac), 400, 400);
        Bitmap bitmap6 = resizeBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.img_trasua), 400, 400);
        Bitmap bitmap7 = resizeBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.img_caphe), 400, 400);
        Bitmap bitmap8 = resizeBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.img_nuocngot), 400, 400);
        Bitmap bitmap9 = resizeBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.img_sua), 400, 400);
        Bitmap bitmap10 = resizeBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.img_khac_1), 400, 400);
        Bitmap bitmap11 = resizeBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.img_comsuon), 1080, 1800);
        Bitmap bitmap12 = resizeBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.img_comchienduongchau), 1080, 1080);
        Bitmap bitmap13 = resizeBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.img_mixao), 1080, 1080);
        Bitmap bitmap14 = resizeBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.img_miy), 1080, 1080);
        Bitmap bitmap15 = resizeBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.img_banhmikepthitnuong), 1080, 1080);
        Bitmap bitmap16 = resizeBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.img_banhmipate), 1080, 1080);
        Bitmap bitmap17 = resizeBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.img_khoaitaychien), 1080, 1080);
        Bitmap bitmap18 = resizeBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.img_chagiohaisan), 1080, 1080);
        Bitmap bitmap19 = resizeBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.img_goicuontomthit), 1080, 1080);
        Bitmap bitmap20 = resizeBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.img_banhxeo), 1080, 1080);
        Bitmap bitmap21 = resizeBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.img_trasuaden), 1080, 1080);
        Bitmap bitmap22 = resizeBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.img_trasuamatcha), 1080, 1080);
        Bitmap bitmap23 = resizeBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.img_cafeespresso), 1080, 1080);
        Bitmap bitmap24 = resizeBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.img_caphemachia), 1080, 1080);
        Bitmap bitmap25 = resizeBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.img_coca), 1080, 1080);
        Bitmap bitmap26 = resizeBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.img_nuocngotcam), 1080, 1080);
        Bitmap bitmap27 = resizeBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.img_suatuoi), 1080, 1080);
        Bitmap bitmap28 = resizeBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.img_suasocola), 1080, 1080);
        Bitmap bitmap29 = resizeBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.img_nuocdua), 1080, 1080);
        Bitmap bitmap30 = resizeBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.img_nuocchanh), 1080, 1080);


        byte[] image1 = getBytesFromBitmap(bitmap1, 50);
        byte[] image2 = getBytesFromBitmap(bitmap2, 50);
        byte[] image3 = getBytesFromBitmap(bitmap3, 50);
        byte[] image4 = getBytesFromBitmap(bitmap4, 50);
        byte[] image5 = getBytesFromBitmap(bitmap5, 50);
        byte[] image6 = getBytesFromBitmap(bitmap6, 50);
        byte[] image7 = getBytesFromBitmap(bitmap7, 50);
        byte[] image8 = getBytesFromBitmap(bitmap8, 50);
        byte[] image9 = getBytesFromBitmap(bitmap9, 50);
        byte[] image10 = getBytesFromBitmap(bitmap10, 100);
        byte[] image11 = getBytesFromBitmap(bitmap11, 100);
        byte[] image12 = getBytesFromBitmap(bitmap12, 100);
        byte[] image13 = getBytesFromBitmap(bitmap13, 100);
        byte[] image14 = getBytesFromBitmap(bitmap14, 100);
        byte[] image15 = getBytesFromBitmap(bitmap15, 100);
        byte[] image16 = getBytesFromBitmap(bitmap16, 100);
        byte[] image17 = getBytesFromBitmap(bitmap17, 100);
        byte[] image18 = getBytesFromBitmap(bitmap18, 100);
        byte[] image19 = getBytesFromBitmap(bitmap19, 100);
        byte[] image20 = getBytesFromBitmap(bitmap20, 100);
        byte[] image21 = getBytesFromBitmap(bitmap21, 100);
        byte[] image22 = getBytesFromBitmap(bitmap22, 100);
        byte[] image23 = getBytesFromBitmap(bitmap23, 100);
        byte[] image24 = getBytesFromBitmap(bitmap24, 100);
        byte[] image25 = getBytesFromBitmap(bitmap25, 100);
        byte[] image26 = getBytesFromBitmap(bitmap26, 100);
        byte[] image27 = getBytesFromBitmap(bitmap27, 100);
        byte[] image28 = getBytesFromBitmap(bitmap28, 100);
        byte[] image29 = getBytesFromBitmap(bitmap29, 100);
        byte[] image30 = getBytesFromBitmap(bitmap30, 100);


        // Insert categories into the database
        // Giả sử bạn đã có đối tượng SQLiteDatabase db

// Cơm
        insertProduct(db, 1, 1, "Cơm Tấm Sườn Nướng", image11, 30000.0, "Cơm tấm với sườn nướng thơm ngon, kèm trứng ốp la và dưa leo, cà chua. Món ăn truyền thống đầy hương vị.", 1, 999);
        insertProduct(db, 1, 2, "Cơm Chiên Dương Châu", image12, 32000.0, "Cơm chiên với tôm, thịt heo, rau củ và trứng. Hương vị đậm đà và đầy đủ dinh dưỡng.", 1, 100);

// Mỳ
        insertProduct(db, 2, 1, "Mỳ Xào Thập Cẩm", image13, 35000.0, "Mỳ xào với nhiều loại hải sản tươi ngon và rau củ, nước sốt đặc biệt.", 1, 200);
        insertProduct(db, 2, 2, "Mỳ Ý Bolognese", image14, 37000.0, "Mỳ Ý với sốt Bolognese truyền thống, thịt bằm và phô mai parmesan.", 1, 300);

// Bánh Mỳ
        insertProduct(db, 3, 1, "Bánh Mỳ Kẹp Thịt Nướng", image15, 20000.0, "Bánh mì kẹp thịt nướng với rau sống tươi ngon và sốt đặc biệt.", 1, 500);
        insertProduct(db, 3, 2, "Bánh Mỳ Pate Đặc Biệt", image16, 22000.0, "Bánh mì với pate và thịt nguội, kèm rau sống và sốt mayonnaise.", 1, 700);

// Đồ Ăn Vặt
        insertProduct(db, 4, 1, "Khoai Tây Chiên Giòn", image17, 15000.0, "Khoai tây chiên giòn rụm, kèm sốt mayonnaise và ketchup.", 1, 800);
        insertProduct(db, 4, 2, "Chả Giò Hải Sản", image18, 18000.0, "Chả giò với hải sản tươi ngon, rau củ và nước chấm chua ngọt.", 1, 10);

// Đồ Ăn Khác
        insertProduct(db, 5, 1, "Gỏi Cuốn Tôm Thịt", image19, 20000.0, "Gỏi cuốn với tôm, thịt heo, rau sống và nước chấm đậu phộng.", 1, 55);
        insertProduct(db, 5, 2, "Bánh Xèo", image20, 22000.0, "Bánh xèo với nhân thịt heo, tôm và giá đỗ, ăn kèm rau sống và nước chấm.", 1, 1000);

// Trà Sữa
        insertProduct(db, 6, 1, "Trà Sữa Đen", image21, 35000.0, "Trà sữa đen với trân châu mềm mịn và vị ngọt thanh.", 1, 2100);
        insertProduct(db, 6, 2, "Trà Sữa Matcha", image22, 38000.0, "Trà sữa matcha thơm ngon với lớp bọt sữa mịn và trân châu.", 1, 1543);

// Cà Phê
        insertProduct(db, 7, 1, "Cà Phê Espresso", image23, 25000.0, "Cà phê espresso đậm đà, phục vụ với một lớp crema dày và mịn.", 1, 1234);
        insertProduct(db, 7, 2, "Cà Phê Americano", image24, 27000.0, "Cà phê Americano với hương vị mạnh mẽ, pha với nước nóng.", 1, 12412);

// Nước Ngọt
        insertProduct(db, 8, 1, "Nước Ngọt Cola", image25, 12000.0, "Nước ngọt cola với hương vị đặc trưng và sủi bọt.", 1, 124);
        insertProduct(db, 8, 2, "Nước Ngọt Cam", image26, 13000.0, "Nước ngọt cam với vị chua ngọt thanh mát.", 1, 124);

// Sữa
        insertProduct(db, 9, 1, "Sữa Tươi", image27, 15000.0, "Sữa tươi nguyên chất, giàu canxi và vitamin.", 1, 14);
        insertProduct(db, 9, 2, "Sữa Socola", image28, 17000.0, "Sữa socola ngọt ngào với hương vị socola đậm đà.", 1, 124);

// Nước Khác
        insertProduct(db, 10, 1, "Nước Dừa", image29, 20000.0, "Nước dừa tươi mát, giàu vitamin và khoáng chất.", 1, 124);
        insertProduct(db, 10, 2, "Nước Chanh", image30, 18000.0, "Nước chanh tươi với hương vị chua ngọt và tinh khiết.", 1, 14);

    }

    private void insertProduct(SQLiteDatabase db, int idCategories, int idShop, String name, byte[] image, Double price, String note, int status, int sold) {
        ContentValues values = new ContentValues();

        values.put(COLUMN_ID_CATEGORIES, idCategories);
        values.put(COLUMN_ID_SHOP, idShop);
        values.put(COLUMN_NAME_PRODUCT, name);
        values.put(COLUMN_IMAGE_PRODUCT, image);
        values.put(COLUMN_PRICE_PRODUCT, price);
        values.put(COLUMN_NOTE_PRODUCT, note);
        values.put(COLUMN_PRODUCT_SOLD, sold);
        values.put(COLUMN_STATUS_PRODUCT, status);

        db.insert(TABLE_PRODUCT, null, values);


    }

    private void insertShop(SQLiteDatabase db, int idUser, String name, String address, byte[] image, int status) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID_USER, idUser);
        values.put(COLUMN_NAME_SHOP, name);
        values.put(COLUMN_ADDRESS_SHOP, address);
        values.put(COLUMN_IMAGE_SHOP, image);
        values.put(COLUMN_STATUS_SHOP, status);

        db.insert(TABLE_SHOP, null, values);
    }

    private void insertCategory(SQLiteDatabase db, int shopId, String name, byte[] image) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID_SHOP, shopId);
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_IMAGE_CATEGORIES, image);
        db.insert(TABLE_CATEGORIES, null, contentValues);
    }


    // img fofmat
    public Bitmap resizeBitmap(Bitmap bitmap, int maxWidth, int maxHeight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        float scaleWidth = ((float) maxWidth) / width;
        float scaleHeight = ((float) maxHeight) / height;
        float scale = Math.min(scaleWidth, scaleHeight);

        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
        bitmap.recycle();
        return resizedBitmap;
    }

    public byte[] getBytesFromBitmap(Bitmap bitmap, int quality) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, quality, stream);
        return stream.toByteArray();
    }


}
