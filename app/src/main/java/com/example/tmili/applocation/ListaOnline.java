package com.example.tmili.applocation;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;


import com.example.tmili.applocation.databinding.ActivityListaOnlineBinding;
import com.example.tmili.applocation.databinding.OnlineBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ListaOnline extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference localRef;
    private ActivityListaOnlineBinding mBinding;
    private FirebaseRecyclerAdapter<Local, LocalHolder> mAdapter;
    private String ref = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding= DataBindingUtil.setContentView(this,R.layout.activity_lista_online);


        Intent filtros =getIntent();
        ref = filtros.getStringExtra("filtro");

        database = FirebaseDatabase.getInstance();
        localRef =database.getReference(ref);

        mBinding.listaOnline.setHasFixedSize(true);
        mBinding.listaOnline.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new FirebaseRecyclerAdapter<Local, LocalHolder>(Local.class, R.layout.online, LocalHolder.class, localRef) {
            @Override
            public void populateViewHolder(LocalHolder localViewHolder, Local local, int position) {
                localViewHolder.setLocal(local);
            }
        };

        mBinding.listaOnline.setAdapter(mAdapter);

    }

    public static class LocalHolder extends RecyclerView.ViewHolder {
        OnlineBinding mBinding;

        public LocalHolder(View view ) {
            super(view);
            mBinding = DataBindingUtil.bind(view);
        }

        public void setLocal(Local local) {
            mBinding.setLocal(local);
        }
    }
}
