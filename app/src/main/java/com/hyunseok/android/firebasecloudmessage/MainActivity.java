package com.hyunseok.android.firebasecloudmessage;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference userRef;

    EditText et_message, et_id, et_pw;
    TextView tv_token;
    ListView listView;
    Button btn_send;

    ListAdapter adapter;
    List<User> datas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance();
        userRef = database.getReference("user");

        tv_token = (TextView) findViewById(R.id.tv_token);
        et_message = (EditText) findViewById(R.id.et_message);
        et_id = (EditText) findViewById(R.id.et_id);
        et_pw = (EditText) findViewById(R.id.et_pw);
        listView = (ListView) findViewById(R.id.listView);
        adapter = new ListAdapter(this, datas);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user = datas.get(position);
                tv_token.setText(user.getToken());
            }
        });
    }

    public void sendNotification(View view) {
        String msg = et_message.getText().toString();

        if(!"".equals(msg)) { // 공백이 아닐때(= 입력값이 있을 때)

        }
    }

    public void signIn(View view) {
        final String id = et_id.getText().toString();
        final String pw = et_pw.getText().toString();

        userRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() > 0){
                    String fbPw = dataSnapshot.child("password").getValue().toString();

                    if(fbPw.equals(pw)){
                        addToken();
                        setList();
                    }else{
                        Toast.makeText(MainActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "ID가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void addToken() {
        final String id = et_id.getText().toString();
        userRef.child(id).child("token").setValue(getToken());
    }

    public void setList() {
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                datas.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) { // user 하위 value 들을 다 가져옴
                    User user = data.getValue(User.class);
                    user.setId(data.getKey());
                    datas.add(user);
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public String getToken() {
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.e("Token===========", token);
        return token;
    }
}

class ListAdapter extends BaseAdapter {

    Context context;
    List<User> datas;
    LayoutInflater inflater;

    public ListAdapter(Context context, List<User> datas) {
        this.context = context;
        this.datas = datas;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null)
            convertView = inflater.inflate(R.layout.item_list, null);

        User user = datas.get(position);
        TextView userId = (TextView) convertView.findViewById(R.id.tv_userId);
        userId.setText(user.getId());
        return convertView;
    }
}