import org.jsoup.Jsoup;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

//mvn exec:java -Dexec.mainClass="Main"
//mvn compile


public class Main {
    static boolean newUserFlag = false;

    public static void main(String[] args) throws MessagingException, IOException, InterruptedException {
        /*UserRepo userRepo = new UserRepo();
        User user = new User();
        user.setName("f");
        User user = userRepo.getUser(1l);
        System.out.println(user.getNewTable().size());
        System.out.println(user.getOldTable().size());*/
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


        /*user.getOldTable().put("https://www.youtube.com", Parser.getHTML("https://www.youtube.com"));
        user.getOldTable().put("https://proselyte.net", Parser.getHTML("https://proselyte.net"));
        user.getOldTable().put("https://news.yahoo.com", Parser.getHTML("https://news.yahoo.com"));
        user.getOldTable().put("https://www.google.com", Parser.getHTML("https://www.google.com"));
//        4
        user.getOldTable().put("https://www.twitch.tv", Parser.getHTML("https://www.twitch.tv"));
        user.getOldTable().put("https://3dnews.ru", Parser.getHTML("https://3dnews.ru"));
        user.getOldTable().put("https://4pda.ru", Parser.getHTML("https://4pda.ru"));
//        3 removed*/

        /*user.getOldTable().put("https://www.youtube.com", Jsoup.connect("https://www.youtube.com").get().toString());
        user.getOldTable().put("https://proselyte.net", Jsoup.connect("https://proselyte.net").get().toString());
        user.getOldTable().put("https://news.yahoo.com", Jsoup.connect("https://news.yahoo.com").get().toString());
        user.getOldTable().put("https://www.google.com", Jsoup.connect("https://www.google.com").get().toString());
//        4
        user.getOldTable().put("https://www.twitch.tv", Jsoup.connect("https://www.twitch.tv").get().toString());
        user.getOldTable().put("https://3dnews.ru", Jsoup.connect("https://3dnews.ru").get().toString());
        user.getOldTable().put("https://4pda.ru", Jsoup.connect("https://4pda.ru").get().toString());
//        3 removed

        System.out.println(user.getOldTable().size());
        userRepo.saveUser(user);*/

        /*user.getNewTable().put("https://www.youtube.com", Parser.getHTML("https://www.youtube.com"));
        user.getNewTable().put("https://proselyte.net", Parser.getHTML("https://proselyte.net"));
        user.getNewTable().put("https://news.yahoo.com", Parser.getHTML("https://news.yahoo.com"));
        user.getNewTable().put("https://www.google.com", Parser.getHTML("https://www.google.com"));
//        4
        user.getNewTable().put("https://ya.ru", Parser.getHTML("https://ya.ru"));
        user.getNewTable().put("https://vk.com", Parser.getHTML("https://vk.com"));
        user.getNewTable().put("https://habr.com/ru/company/luxoft/blog/270383/", Parser.getHTML("https://habr.com/ru/company/luxoft/blog/270383/"));
        user.getNewTable().put("https://ru.stackoverflow.com/questions", Parser.getHTML("https://ru.stackoverflow.com/questions"));
//  new 4*/

        /*user.getNewTable().put("https://www.youtube.com", Jsoup.connect("https://www.youtube.com").get().toString());
        user.getNewTable().put("https://proselyte.net", Jsoup.connect("https://proselyte.net").get().toString());
        user.getNewTable().put("https://news.yahoo.com", Jsoup.connect("https://news.yahoo.com").get().toString());
        user.getNewTable().put("https://www.google.com", Jsoup.connect("https://www.google.com").get().toString());
//        4
        user.getNewTable().put("https://ya.ru", Jsoup.connect("https://ya.ru").get().toString());
        user.getNewTable().put("https://vk.com", Jsoup.connect("https://vk.com").get().toString());
        user.getNewTable().put("https://habr.com/ru/company/luxoft/blog/270383/", Jsoup.connect("https://habr.com/ru/company/luxoft/blog/270383/").get().toString());
        user.getNewTable().put("https://ru.stackoverflow.com/questions", Jsoup.connect("https://ru.stackoverflow.com/questions").get().toString());
//  new 4

        System.out.println(user.getNewTable().size());
        userRepo.updateUser(user);*/


/*//        String test = "[Ilya Mikhailov <oxy.lusha@gmail.com>]";
//        String test = "[Ilya  Mikhailov <iggyrayady@icloud.com>]";
//        String test = "[=?UTF-8?B?0JjQu9GM0Y8g0JzQuNGF0LDQudC70L7Qsg==?= <ilya7887@mail.ru>]";
        List<String> list2 = Arrays.asList(Objects.requireNonNull(mesHead).split(" "));

//        System.out.println(list2.size());
        for (String s : list2) {
//            System.out.println(s);
            if (s.contains("<") || s.contains(">")) {
                mesHead = s;
                break;
            }
        }
        mesHead = mesHead.replace(']', ' ').replace('<', ' ').replace('>', ' ').trim();
        System.out.println(mesHead);*/

    /*res = res.replace('\n', ' ').trim();
            List<String> list2 = new ArrayList<>();
            List<String> list = Arrays.asList(Objects.requireNonNull(res).split(","));
//        System.out.println(list.size());
            for (String s : list) {
                if (s.contains(" ") || s.contains("\n")) {
                    s = s.trim();
                    list2.add(s);
//                Jsoup.connect(s).get();
                } else {
                    list2.add(s);
                }
            }*/

    /*public static void checkDate(User user) {
        if (user.getCheckDate().equals(LocalDate.now())) {
            user.getNewTable().clear();
            System.out.println(1);
        } else {
            user.getOldTable().clear();
            user.getOldTable().putAll(user.getNewTable());
            user.getNewTable().clear();
            System.out.println(2);
        }
    }*/

    /*public static String checkUpdate(
            User user,
            String URL
    ) {
        Iterable<String> urlList;
        List<String> result = new ArrayList<>();
        if (URL.equals(null) | URL.length() == 0) {
            urlList = user.getOldTable().keySet();
            System.out.println(urlList);
        } else {
            urlList = Arrays.asList(URL.split(","));
            System.out.println(urlList);
        }
        for (String s : urlList) {
            if (!user.getOldTable().get(s).equals(user.getNewTable().get(s))) {
                result.add(s);
            }
        }
        return result.toString();
    }*/

    /*public static Map<String, String> checkKeys(
            Set<String> oldKeysSet,
            Set<String> newKeysSet
    ) {
        Map<String, String> resultMap = new HashMap<>();
        List<String> dKeys = new ArrayList<>();
        StringBuilder sKeys = new StringBuilder();

        if (!oldKeysSet.equals(newKeysSet)) {
////////
            for (String oldK : oldKeysSet) {
                if (!newKeysSet.contains(oldK)) {//was removed
                    dKeys.add(oldK);
                } else {
                    sKeys.append(oldK + ",");//ssssssssssssss
                }
            }
            if (dKeys.isEmpty()) {
                resultMap.put("removed", "Не было удаленных страниц");
            } else {
                resultMap.put("removed", dKeys.toString());
                dKeys.clear();
            }
////////
            for (String newK : newKeysSet) {
                if (!oldKeysSet.contains(newK)) {//was added
                    dKeys.add(newK);
                }
            }
            if (dKeys.isEmpty()) {
                resultMap.put("added", "Не было новых страниц");
            } else {
                resultMap.put("added", dKeys.toString());
                dKeys.clear();
            }
////////
        } else {
            resultMap.put("removed", "Не было удаленных страниц");
            resultMap.put("added", "Не было новых страниц");
            return resultMap;
        }
        resultMap.put("reuse", sKeys.toString());
        return resultMap;
    }*/





