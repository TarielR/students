package model;


import java.sql.ResultSet;
import java.sql.SQLException;

public class Group {
    private int groupId;
    private String groupName;
    private int curator;
    private String speciality;

    public Group(ResultSet rs) throws SQLException {
        setGroupId(rs.getInt(1));
        setGroupName(rs.getString(2));
        setCurator(rs.getInt(3));
        setSpeciality(rs.getString(4));
    }

    public Group() {  }

    public int getGroupId() { return groupId; }

    public void setGroupId(int groupid) { this.groupId = groupid; }

    public String getGroupName() { return groupName; }

    public void setGroupName(String groupName) { this.groupName = groupName; }

    public int getCurator() { return curator; }

    public void setCurator(int curatorId) { this.curator = curator; }

    public String getSpeciality() { return speciality; }

    public void setSpeciality(String speciality) { this.speciality = speciality; }


    @Override
    public String toString() {
        return "Group{" +
                "groupId=" + groupId +
                ", groupName='" + groupName + '\'' + '\n' +
                ", curator=" + curator +
                ", speciality='" + speciality + '\'' +
                '}';
    }
}
