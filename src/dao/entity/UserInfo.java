package dao.entity;

public class UserInfo {

    private String uname;
    private String paswrd;
    private int purview;

    // 构造函数
    public UserInfo(String uname, String paswrd, int purview) {
        this.uname = uname;
        this.paswrd = paswrd;
        this.purview = purview;
    }

    // getter 和 setter 方法
    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getPaswrd() {
        return paswrd;
    }

    public void setPaswrd(String paswrd) {
        this.paswrd = paswrd;
    }

    public int getPurview() {
        return purview;
    }

    public void setPurview(int purview) {
        this.purview = purview;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "uname='" + uname + '\'' +
                ", paswrd='" + paswrd + '\'' +
                ", purview=" + purview +
                '}';
    }
}
