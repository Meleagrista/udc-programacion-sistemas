package es.udc.ps.p34dorio.module;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BaseActivity extends AppCompatActivity {

    private Unbinder mUnbinder;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {

        super.setContentView(layoutResID);
        mUnbinder = ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {

        if (mUnbinder != null) {
            mUnbinder.unbind();
        }

        super.onDestroy();
    }

}
