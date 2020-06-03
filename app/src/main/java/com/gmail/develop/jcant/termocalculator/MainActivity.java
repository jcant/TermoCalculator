package com.gmail.develop.jcant.termocalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.gmail.develop.jcant.termocalculator.tables.G100P.G100P0_100N;
import com.gmail.develop.jcant.termocalculator.tables.G21.G210_200;
import com.gmail.develop.jcant.termocalculator.tables.G23.G230_150N;
import com.gmail.develop.jcant.termocalculator.tables.G23.G230_180;
import com.gmail.develop.jcant.termocalculator.tables.G50M.G50M0_200;
import com.gmail.develop.jcant.termocalculator.tables.G50P.G50P0_100N;
import com.gmail.develop.jcant.termocalculator.tables.G50P.G50P0_150N;
import com.gmail.develop.jcant.termocalculator.tables.G50P.G50P0_200N;
import com.gmail.develop.jcant.termocalculator.tables.G50P.G50P0_300N;
import com.gmail.develop.jcant.termocalculator.tables.G50P.G50P0_400;
import com.gmail.develop.jcant.termocalculator.tables.G50P.G50P0_400N;
import com.gmail.develop.jcant.termocalculator.tables.G50P.G50P0_500N;
import com.gmail.develop.jcant.termocalculator.tables.G50P.G50P0_50N;
import com.gmail.develop.jcant.termocalculator.tables.G50P.G50P70_180N;
import com.gmail.develop.jcant.termocalculator.tables.GradTable;
import com.gmail.develop.jcant.termocalculator.tables.XK.GXK0_150N;
import com.gmail.develop.jcant.termocalculator.tables.XK.GXK0_200N;
import com.gmail.develop.jcant.termocalculator.tables.XK.GXK0_300N;
import com.gmail.develop.jcant.termocalculator.tables.XK.GXK0_400N;
import com.gmail.develop.jcant.termocalculator.tables.XK.GXKmA0_400;
import com.gmail.develop.jcant.termocalculator.tables.XK.GXKmV0_400;
import com.gmail.develop.jcant.termocalculator.tables.XK.GXKmVSVRK0_400;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private EditText et_SignalValue;
    private Switch swNorm;
    private ToggleButton tglButton_50P;
    private ToggleButton tglButton_50M;
    private ToggleButton tglButton_100P;
    private ToggleButton tglButton_21GR;
    private ToggleButton tglButton_23GR;
    private ToggleButton tglButton_XK;
    private ToggleButton tglButton_XKL;
    private ToggleButton tglButton_mV;
    private ToggleButton tglButton_mA;
    private ToggleButton tglButton_mVSVRK;
    private Spinner spScale;
    private TextView tv_Result;
    private TextView tv_scaleBounds;
    private TextView tv_warning;
    private GradTable gradTable;

    List<String> scalesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_SignalValue  = findViewById(R.id.et_SignalValue);
        swNorm          = findViewById(R.id.swNorm);
        tglButton_50P   = findViewById(R.id.tglButton_50P);
        tglButton_50M   = findViewById(R.id.tglButton_50M);
        tglButton_100P  = findViewById(R.id.tglButton_100P);
        tglButton_21GR  = findViewById(R.id.tglButton_21GR);
        tglButton_23GR  = findViewById(R.id.tglButton_23GR);
        tglButton_XK    = findViewById(R.id.tglButton_XK);
        tglButton_XKL   = findViewById(R.id.tglButton_XKL);
        tglButton_mV    = findViewById(R.id.tglButton_mV);
        tglButton_mA    = findViewById(R.id.tglButton_mA);
        tglButton_mVSVRK= findViewById(R.id.tglButton_mvSVRK);
        spScale         = findViewById(R.id.spScale);
        tv_Result       = findViewById(R.id.tv_Result);
        tv_scaleBounds  = findViewById(R.id.tv_scaleBounds);
        tv_warning  = findViewById(R.id.tv_warning);

        tglButton_50P.setOnClickListener(this);
        tglButton_50M.setOnClickListener(this);
        tglButton_100P.setOnClickListener(this);
        tglButton_21GR.setOnClickListener(this);
        tglButton_23GR.setOnClickListener(this);
        tglButton_XK.setOnClickListener(this);
        tglButton_XKL.setOnClickListener(this);
        tglButton_mV.setOnClickListener(this);
        tglButton_mA.setOnClickListener(this);
        tglButton_mVSVRK.setOnClickListener(this);

        swNorm.setOnClickListener(this);
        spScale.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectTable();
                setScaleBoundsInfo();
                setTemperatureValue();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        scalesList = new ArrayList<>();
        onNormClick();
        onClick(tglButton_50P);

        et_SignalValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setTemperatureValue();
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.swNorm) {
            onNormClick();
        } else if(view.getId() == R.id.tglButton_mV || view.getId() == R.id.tglButton_mA || view.getId() == R.id.tglButton_mvSVRK) {
            toggleSVRKGroup(view.getId());
        } else {
            toggleGroup(view.getId());
        }

        setScalesList();
        selectTable();
        setScaleBoundsInfo();

        setTemperatureValue();
    }

    private void toggleGroup(int ID) {
        tglButton_50P.setChecked(tglButton_50P.getId() == ID);
        tglButton_50M.setChecked(tglButton_50M.getId() == ID);
        tglButton_100P.setChecked(tglButton_100P.getId() == ID);
        tglButton_21GR.setChecked(tglButton_21GR.getId() == ID);
        tglButton_23GR.setChecked(tglButton_23GR.getId() == ID);
        tglButton_XK.setChecked(tglButton_XK.getId() == ID);
        tglButton_XKL.setChecked(tglButton_XKL.getId() == ID);

        tglButton_mV.setEnabled(tglButton_XK.getId() == ID && !swNorm.isChecked());
        tglButton_mA.setEnabled(tglButton_XK.getId() == ID && !swNorm.isChecked());
        tglButton_mVSVRK.setEnabled(tglButton_XK.getId() == ID && !swNorm.isChecked());
    }

    private void toggleSVRKGroup(int ID) {
        tglButton_mV.setChecked(tglButton_mV.getId() == ID);
        tglButton_mA.setChecked(tglButton_mA.getId() == ID);
        tglButton_mVSVRK.setChecked(tglButton_mVSVRK.getId() == ID);
    }

    private void onNormClick() {
        tglButton_50M.setEnabled(!swNorm.isChecked());
        tglButton_21GR.setEnabled(!swNorm.isChecked());
        tglButton_100P.setEnabled(swNorm.isChecked());
        tglButton_XKL.setEnabled(swNorm.isChecked());

        tglButton_mV.setEnabled(!swNorm.isChecked() && tglButton_XK.isChecked());
        tglButton_mA.setEnabled(!swNorm.isChecked() && tglButton_XK.isChecked());
        tglButton_mVSVRK.setEnabled(!swNorm.isChecked() && tglButton_XK.isChecked());

        if (tglButton_50M.isChecked() || tglButton_21GR.isChecked() || tglButton_100P.isChecked() || tglButton_XKL.isChecked()) {
            toggleGroup(tglButton_50P.getId());
        }
    }

    private void setScalesList() {
        scalesList.clear();
        if (!swNorm.isChecked()) {
            if (tglButton_23GR.isChecked()) scalesList.add(getString(R.string.scale0_180));
            if (tglButton_50M.isChecked() || tglButton_21GR.isChecked()) scalesList.add(getString(R.string.scale0_200));
            if (tglButton_50P.isChecked() || tglButton_XK.isChecked()) scalesList.add(getString(R.string.scale0_400));
        }
        if (swNorm.isChecked()) {
            if (tglButton_50P.isChecked()) scalesList.add(getString(R.string.scale0_50));
            if (tglButton_50P.isChecked() || tglButton_100P.isChecked()) scalesList.add(getString(R.string.scale0_100));
            if (tglButton_50P.isChecked() || tglButton_23GR.isChecked() || tglButton_XK.isChecked() || tglButton_XKL.isChecked()) scalesList.add(getString(R.string.scale0_150));
            if (tglButton_50P.isChecked() || tglButton_XK.isChecked() || tglButton_XKL.isChecked()) scalesList.add(getString(R.string.scale0_200));
            if (tglButton_50P.isChecked() || tglButton_XK.isChecked() || tglButton_XKL.isChecked()) scalesList.add(getString(R.string.scale0_300));
            if (tglButton_50P.isChecked() || tglButton_XK.isChecked() || tglButton_XKL.isChecked()) scalesList.add(getString(R.string.scale0_400));
            if (tglButton_50P.isChecked()) scalesList.add(getString(R.string.scale0_500));
            if (tglButton_50P.isChecked()) scalesList.add(getString(R.string.scale70_180));
        }

        Log.d(TAG,"scalesList: " + scalesList.toString());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, scalesList.toArray(new String[0]));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spScale.setAdapter(adapter);
    }

    private void selectTable() {
        String scale = spScale.getSelectedItem().toString();
        if (!swNorm.isChecked()) {
            if (tglButton_50P.isChecked() && scale.equals(getString(R.string.scale0_400))) gradTable = new G50P0_400();
            if (tglButton_50M.isChecked() && scale.equals(getString(R.string.scale0_200))) gradTable = new G50M0_200();
            if (tglButton_21GR.isChecked() && scale.equals(getString(R.string.scale0_200))) gradTable = new G210_200();
            if (tglButton_23GR.isChecked() && scale.equals(getString(R.string.scale0_180))) gradTable = new G230_180();
            if (tglButton_XK.isChecked() && scale.equals(getString(R.string.scale0_400))) {
                if (tglButton_mV.isChecked()) gradTable = new GXKmV0_400();
                if (tglButton_mA.isChecked()) gradTable = new GXKmA0_400();
                if (tglButton_mVSVRK.isChecked()) gradTable = new GXKmVSVRK0_400();
            }
        }
        if (swNorm.isChecked()) {
            if (tglButton_50P.isChecked()) {
                if (scale.equals(getString(R.string.scale0_50))) gradTable = new G50P0_50N();
                if (scale.equals(getString(R.string.scale0_100))) gradTable = new G50P0_100N();
                if (scale.equals(getString(R.string.scale0_150))) gradTable = new G50P0_150N();
                if (scale.equals(getString(R.string.scale0_200))) gradTable = new G50P0_200N();
                if (scale.equals(getString(R.string.scale0_300))) gradTable = new G50P0_300N();
                if (scale.equals(getString(R.string.scale0_400))) gradTable = new G50P0_400N();
                if (scale.equals(getString(R.string.scale0_500))) gradTable = new G50P0_500N();
                if (scale.equals(getString(R.string.scale70_180))) gradTable = new G50P70_180N();
            }
            if (tglButton_100P.isChecked() && scale.equals(getString(R.string.scale0_100))) gradTable = new G100P0_100N();
            if (tglButton_23GR.isChecked() && scale.equals(getString(R.string.scale0_150))) gradTable = new G230_150N();
            if (tglButton_XK.isChecked()) {
                if (scale.equals(getString(R.string.scale0_150))) gradTable = new GXK0_150N();
                if (scale.equals(getString(R.string.scale0_200))) gradTable = new GXK0_200N();
                if (scale.equals(getString(R.string.scale0_300))) gradTable = new GXK0_300N();
                if (scale.equals(getString(R.string.scale0_400))) gradTable = new GXK0_400N();
            }
            if (tglButton_XKL.isChecked()) {
                if (scale.equals(getString(R.string.scale0_150))) gradTable = new GXK0_150N();
                if (scale.equals(getString(R.string.scale0_200))) gradTable = new GXK0_200N();
                if (scale.equals(getString(R.string.scale0_300))) gradTable = new GXK0_300N();
                if (scale.equals(getString(R.string.scale0_400))) gradTable = new GXK0_400N();
            }
        }
    }

    private void setScaleBoundsInfo() {
        if (gradTable != null) {
            tv_scaleBounds.setText(getString(R.string.scale_bounds, gradTable.getParamMin(), gradTable.getParamMax()));
        } else {
            tv_scaleBounds.setText(getString(R.string.scale_bounds_empty));
        }
    }

    private void setTemperatureValue() {
        String sParam = et_SignalValue.getText().toString();
        if ((gradTable != null) && (!sParam.isEmpty())) {
            double param = Double.parseDouble(sParam);

            if (param < gradTable.getParamMin()) {
                tv_warning.setText("мало!");
                tv_Result.setText(getString(R.string.err_value));
                return;
            }
            if (param > gradTable.getParamMax()) {
                tv_warning.setText("много!");
                tv_Result.setText(getString(R.string.err_value));
                return;
            }

            tv_warning.setText("");

            double tempr = gradTable.getTemperature(param);
            tv_Result.setText(getString(R.string.result, tempr));
        } else {
            tv_Result.setText(getString(R.string.null_value));
        }
    }
}