package core;

import dao.entity.UserInfo;
import lombok.Data;

@Data
public class StateData {

    public static StateData INS = new StateData();

    public StateData() {

    }

    private UserInfo currentuser;


}
