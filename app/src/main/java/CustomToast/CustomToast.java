package CustomToast;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.du_an_1.R;


public class CustomToast {

    public static void show(Context context, String message) {
        // Sử dụng LayoutInflater để tạo bố cục tùy chỉnh
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.custom_toast_layout, null);

        // Thiết lập biểu tượng và văn bản
        ImageView toastIcon = layout.findViewById(R.id.toast_icon);
        TextView toastText = layout.findViewById(R.id.toast_text);

        // Sử dụng biểu tượng mặc định trong custom_toast_layout.xml
        // Bạn không cần phải thiết lập lại biểu tượng nếu bạn muốn giữ nguyên biểu tượng mặc định
        toastText.setText(message);

        // Tạo và hiển thị Toast
        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    public static void show(Context context, String message, int iconResId) {
        // Sử dụng LayoutInflater để tạo bố cục tùy chỉnh
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.custom_toast_layout, null);

        // Thiết lập biểu tượng và văn bản
        ImageView toastIcon = layout.findViewById(R.id.toast_icon);
        TextView toastText = layout.findViewById(R.id.toast_text);

        toastIcon.setImageResource(iconResId);
        toastText.setText(message);

        // Tạo và hiển thị Toast
        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }
}