import org.jsoup.Jsoup;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Main {
    static boolean newUserFlag = false;

    public static void main(String[] args) throws MessagingException, IOException, InterruptedException {

        User user = null;
        UserRepo userRepo = new UserRepo();
        List<User> optional = userRepo.getUser();
        if (optional.size() == 0) {
            user = new User();
            user.setName("Test");
            newUserFlag = true;
        } else {
            user = optional.get(0);
        }

        for (; ; ) {
            Map<String, String> resultMap = MailUtil.read();

            while (resultMap != null) {

                String mesHead = resultMap.get("mesHead");
                String mesBody = resultMap.get("mesBody");
                String mesData = resultMap.get("mesData");
                if (user.getMesData() == null || !user.getMesData().equals(mesData)) {
                    user.setMesData(mesData);
                    if (mesBody == null || mesBody.length() > 5) {
                        CheckTools.checkUserDate(user);

                        mesHead = CheckTools.getFrom(mesHead);

                        List<String> list = CheckTools.checkInput(mesBody);
                        for (String s : list) {
                            user.getNewTable().put(s, Jsoup.connect(s.trim()).get().toString());
                        }
                        resultMap.clear();
                        resultMap = CheckTools.checkKeys(user.getOldTable().keySet(), user.getNewTable().keySet());

                        MailUtil.sendMessage("testAnswer",
                                resultMap.get("removed"),
                                resultMap.get("added"),
                                CheckTools.checkUpdate(user, resultMap.get("reuse")),
                                mesHead);
                        if (newUserFlag) {
                            userRepo.saveUser(user);
                        } else {
                            userRepo.updateUser(user);
                        }
                    }
                    System.out.println("Нет корректного URL");
                    break;
                }
                System.out.println("Нет новых сообщений");
                break;
            }
            Thread.currentThread().sleep(10000l);
        }
    }
}
