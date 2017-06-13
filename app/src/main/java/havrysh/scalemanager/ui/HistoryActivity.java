package havrysh.scalemanager.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.widget.Button;
import android.widget.DatePicker;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import havrysh.scalemanager.R;

public class HistoryActivity extends AppCompatActivity {

    @BindView(R.id.date_button)
    Button dateButton;

    private Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ButterKnife.bind(this);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_contaiener, PiecesFragment.newInstance())
                .commitNowAllowingStateLoss();

    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        onDateChanged(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
    }

    private void onDateChanged(int year, int month, int dayOfMonth) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        dateButton.setText(DateUtils.formatDateTime(this, calendar.getTimeInMillis(), 0));

        Calendar temp = Calendar.getInstance();
        temp.setTimeInMillis(calendar.getTimeInMillis());

        temp.set(Calendar.HOUR_OF_DAY, calendar.getMinimum(Calendar.HOUR_OF_DAY));
        temp.set(Calendar.MINUTE, calendar.getMinimum(Calendar.MINUTE));
        long dayStart = temp.getTimeInMillis();

        temp.set(Calendar.HOUR_OF_DAY, calendar.getMaximum(Calendar.HOUR_OF_DAY));
        temp.set(Calendar.MINUTE, calendar.getMaximum(Calendar.MINUTE));
        long dayEnd = temp.getTimeInMillis();

        ((PiecesFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_contaiener))
                .invalidateItems(dayStart, dayEnd);
    }

    @OnClick(R.id.date_button)
    void onButtonClick() {
        new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        onDateChanged(year, month, dayOfMonth);
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH))
                .show();
    }
}
