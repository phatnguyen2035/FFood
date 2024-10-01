package fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.du_an_1.R;

import dao.ShopDAO;
import model.Shop;

public class ManageBuyerFragment extends Fragment {
    private int role, idUser;
    private String user, pass, name;
    private long cccd, phone;
    private ShopDAO shopDAO;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_manage_buyer, container, false);

        shopDAO = new ShopDAO(getContext());

        //get User
        getUserFromSharedPreferences();

        // Find the buttons and checkbox after the view is created
        Button btn_register_booth1 = view.findViewById(R.id.btn_register_booth1);


        btn_register_booth1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View dialogView = inflater.inflate(R.layout.dialog_booth_register, null);
                builder.setView(dialogView);

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


                //dialog Register
                Button btn_register_booth2 = dialogView.findViewById(R.id.btn_register_booth2);
                CheckBox cb_register = dialogView.findViewById(R.id.cb_booth_register);
                EditText edt_name_register = dialogView.findViewById(R.id.edt_name_shop_register);
                EditText edt_address_register = dialogView.findViewById(R.id.edt_address_shop_register);

                //checkbox
                boolean isChecked = cb_register.isChecked();
                btn_register_booth2.setEnabled(cb_register.isChecked());
                cb_register.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        btn_register_booth2.setEnabled(isChecked);
                    }
                });
                // register
                btn_register_booth2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = edt_name_register.getText().toString();
                        String address = edt_address_register.getText().toString();

                        if (name.length() == 0 || address.length() == 0) {
                            Toast.makeText(getContext(), "Enter complete information", Toast.LENGTH_SHORT).show();
                        } else {

                            int check = shopDAO.addShop(idUser, name, address,null,1);
                            switch (check) {
                                case 1:

                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                                    builder1.setTitle("Messenger");
                                    builder1.setMessage("Sign Up Success");

                                    builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.dismiss();
                                            alertDialog.dismiss();
                                        }
                                    });
                                    AlertDialog alertDialog1 = builder1.create();
                                    alertDialog1.show();
                                    break;
                                case 0:
                                    Toast.makeText(getContext(), "Account has registered for the store", Toast.LENGTH_SHORT).show();
                                    break;
                                case -1:
                                    Toast.makeText(getContext(), "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                });


            }
        });
        return view;
    }

    public void getUserFromSharedPreferences() {
        SharedPreferences share = getContext().getSharedPreferences("User_Login", Context.MODE_PRIVATE);
        role = share.getInt("role", -1);
        idUser = share.getInt("idUser", -1);
        user = share.getString("user", "");
        name = share.getString("name", "");
        phone = share.getLong("phone", -1);
        cccd = share.getLong("cccd", -1);
        role = share.getInt("role", -1);


    }
}
