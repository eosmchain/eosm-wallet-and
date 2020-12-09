package com.token.mangowallet.ui.activity.pinyin;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.LogUtils;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.token.mangowallet.R;
import com.token.mangowallet.view.WaveSideBarView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CountryCodeActivity extends AppCompatActivity {

    @BindView(R.id.countryRecyclerView)
    RecyclerView countryRecyclerView;
    @BindView(R.id.side_view)
    WaveSideBarView sideView;
    @BindView(R.id.topBar)
    QMUITopBar topBar;
    @BindView(R.id.et_search)
    AppCompatEditText etSearch;

    private Unbinder unbinder;
    private CountryCodeAdapter countryCodeAdapter;
    private LinearLayoutManager manager;
    private ArrayList<CountryBean> selectedCountries = new ArrayList<>();
    private ArrayList<CountryBean> allCountries = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        BarUtils.setStatusBarColor(this, ContextCompat.getColor(this, R.color.qmui_config_color_white));
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_code);
        unbinder = ButterKnife.bind(this);
        topBar.setTitle(R.string.str_location_code);
        topBar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                finish();
            }
        });
        allCountries.clear();
        allCountries.addAll(CountryBean.getAll(this, null));
        selectedCountries.clear();
        selectedCountries.addAll(allCountries);
        initRecycler();
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String string = s.toString();
                selectedCountries.clear();
                for (CountryBean country : allCountries) {
                    if (country.name.toLowerCase().contains(string.toLowerCase()))
                        selectedCountries.add(country);
                }
                countryCodeAdapter.update(selectedCountries);
            }
        });

        sideView.setOnTouchLetterChangeListener(new WaveSideBarView.OnTouchLetterChangeListener() {
            @Override
            public void onLetterChange(String letter) {
                int position = countryCodeAdapter.getLetterPosition(letter);
                if (position != -1) {
                    manager.scrollToPositionWithOffset(position, 0);
                }
            }
        });
    }

    private void initRecycler() {
        countryCodeAdapter = new CountryCodeAdapter(this, selectedCountries);
        manager = new LinearLayoutManager(this);
        countryRecyclerView.setLayoutManager(manager);
        countryRecyclerView.setAdapter(countryCodeAdapter);
        countryCodeAdapter.setOnItemClickListener(new CountryCodeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull View view, PyEntity entity, int position) {
                CountryBean country = (CountryBean) entity;
                LogUtils.dTag("countryCodeAdapter==", "code = " + country.code + " locale = " + country.locale + " name = " + country.name);
                Intent data = new Intent();
                data.putExtra("country", country.toJson());
                setResult(RESULT_OK, data);
                finish();
            }
        });
        countryRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
