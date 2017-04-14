package model;


public class Group {
    private int groupId;
    private String groupName;
    private Curator curator;
    private String speciality;

    public int getGroupId() { return groupId; }

    public void setGroupId(int groupid) { this.groupId = groupid; }

    public String getGroupName() { return groupName; }

    public void setGroupName(String groupName) { this.groupName = groupName; }

    public Curator getCurator() { return curator; }

    public void setCurator(Curator curator) { this.curator = curator; }

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
