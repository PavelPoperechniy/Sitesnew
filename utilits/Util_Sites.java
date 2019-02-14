package utilits;

import javafx.application.Platform;
import javafx.scene.control.TextField;
import obgektDB.RolesDB;
import object.Reservation;
import object.Roles;
import object.Sites;
import object.User;
import obgektDB.ReservationDb;
import obgektDB.SitesDB;
import obgektDB.UserDB;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Util_Sites {
    private static Util_Sites instance;
    private static int MILLISECOND = 1000;
    private static int YEAR = 365;
    private static int MOUNTH = 30;

    public static Util_Sites getInstance() {
        if (instance == null) {
            instance = new Util_Sites();
        }
        return instance;
    }

    private Util_Sites() {
    }

    public boolean writeObgectFromDb(Object object) {// Не реализовано
        boolean flag = false;
        if (object instanceof Sites) {
            flag = SitesDB.getInstance().insertObgect((Sites) object);

        } else if (object instanceof User) {
            flag = UserDB.getInstance().insertObgect((User) object);
        } else if (object instanceof Reservation) {
            flag = ReservationDb.getInstance().insertObgect((Reservation) (object));
        } else if (object instanceof Roles) {
            flag = RolesDB.getInstance().insertObgect((Roles)object);

        }
        return flag;
    }

    public  boolean updateObgectDB(Object object) throws SQLException {
        boolean flag = false;
        if (object instanceof Sites) {
            flag = SitesDB.getInstance().update_dataObgectDB(SitesDB.getInstance().updateObgectStm((Sites)object));

        } else if (object instanceof User) {
            flag = UserDB.getInstance().update_dataObgectDB(UserDB.getInstance().updateObgectStm((User)object));
        } else if (object instanceof Reservation) {
            flag = ReservationDb.getInstance().update_dataObgectDB(ReservationDb.getInstance().updateObgectStm((Reservation)object));
        } else if (object instanceof Roles) {
            flag = RolesDB.getInstance().update_dataObgectDB(RolesDB.getInstance().updateObgectStm((Roles)object));


        }
        return flag;
    }


    public boolean chekTextFaild(TextField textField) {
        boolean flag = true;
        if (textField.getText().trim().isEmpty()) {
            flag = false;
        }
        return flag;
    }

    public void chekDate(Sites sites, boolean status) {
        if (!status) {
            chekData_Issue(sites);
        } else checkDate_delivery(sites);
    }

    private void chekData_Issue(Sites sites) {
        StringBuilder str = new StringBuilder();
        Calendar thisDate = new GregorianCalendar();
        long result = thisDate.getTimeInMillis() - (sites.getReservation().getDate_issue()) * MILLISECOND;
        long days = TimeUnit.MILLISECONDS.toDays(result);
        if (days > 0) {
            str.append("В обработке : ");

            int year = (int) days / YEAR;
            if (year > 0) {
                days = days - year * YEAR;
                sites.getReservation().setLongInwork(true);
                str.append("" + year);
                if (year == 1) {
                    str.append(" год");
                }
                if (year > 1 && year < 5) {
                    str.append(" года");
                }
                if (year > 4) {
                    str.append(" лет");
                }
            }

            if (days > 0) {
                int mounth = (int) days / MOUNTH;
                if (mounth > 0) {
                    str.append(" " + mounth);
                    days = days - mounth * MOUNTH;

                    if (mounth == 1) {
                        str.append(" месяц");
                    }
                    if (mounth > 1 && mounth < 5) {
                        str.append(" месяца");
                    }
                    if (mounth > 4) {
                        str.append(" месяцев");
                        sites.getReservation().setLongInwork(true);
                    }
                }


                if (days > 0) {
                    str.append(" " + days);
                    if (days == 1) {
                        str.append(" день");
                    }
                    if (days > 1 && days < 5) {
                        str.append(" дня");
                    }
                    if (days > 4) {
                        str.append(" дней");
                    }
                }
            }
        }

        sites.setTimeStatus(str.toString());
    }

    private void checkDate_delivery(Sites sites) {
        StringBuilder str = new StringBuilder();
        Calendar thisDate = new GregorianCalendar();
        int year = 0;
        int mounth = 0;
        long result = thisDate.getTimeInMillis() - (sites.getReservation().getDate_delivery()) * MILLISECOND;
        long days = TimeUnit.MILLISECONDS.toDays(result);

            str.append("Сдан : ");

        year = (int) days / YEAR;
        if (year > 0) {
            days = days - year * YEAR;
            sites.getReservation().setLongInwork(true);
            str.append("" + year);
            if (year == 1) {
                str.append(" год");
            }
            if (year > 1 && year < 5) {
                str.append(" года");
            }
            if (year > 4) {
                str.append(" лет");
            }
        }

        if (days > 0) {
            mounth = (int) days / MOUNTH;
            if (mounth > 0) {

                days = days - mounth * MOUNTH;
                str.append(" " + mounth);
                if (mounth == 1) {
                    str.append(" месяц");
                }
                if (mounth > 1 && mounth < 5) {
                    str.append(" месяца");
                }
                if (mounth > 4) {
                    str.append(" месяцев");
                    sites.getReservation().setLongInwork(true);
                }

            }


            if (days > 0) {
                str.append(" " + days);
                if (days == 1) {
                    str.append(" день");
                }
                if (days > 1 && days < 5) {
                    str.append(" дня");
                }
                if (days > 4) {
                    str.append(" дней");
                }
            }
        }
        if (year > 0 || mounth > 0 || days > 0) {
            str.append(" тому назад");
        } else {
            str.append(" меньше дня тому назад");
        }
        sites.setTimeStatus(str.toString());
    }


    public void updateListUser(HashMap<String,User> list, User user){
        for(Map.Entry<String,User>entry:list.entrySet()){
            if(entry.getValue().equals(user)){
                Platform.runLater(()->list.remove(user));
                list.put(user.toString(),user);
                return;
            }
        }
    }

    public boolean checkListByKey(HashMap<String,User>list,User user){
        boolean flag = false;
        for (Map.Entry<String,User>entry:list.entrySet()){
            if(entry.getKey().toLowerCase().equals(user.toString().toLowerCase())){
                flag = true;
                return flag;
            }
        }
        return flag;
    }


}
