package havrysh.scalemanager;

import com.raizlabs.android.dbflow.sql.language.SQLCondition;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.ArrayList;
import java.util.List;

public class PieceFacade {
    public Piece save(Piece model) {
        model.setDate(System.currentTimeMillis());
        model.save();
        return model;
    }

    public List<Piece> getAll(Long dateFrom, Long dateTo) {
        List<SQLCondition> conds = new ArrayList<>();
        if (dateFrom != null) {
            conds.add(Piece_Table.date.greaterThanOrEq(dateFrom));
        }
        if (dateTo != null) {
            conds.add(Piece_Table.date.lessThanOrEq(dateTo));
        }
        return new Select().from(Piece.class).where((SQLCondition[]) conds.toArray()).queryList();
    }
}
