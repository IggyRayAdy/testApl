import org.jsoup.Jsoup;

import java.time.LocalDate;
import java.util.*;

public class CheckTools {

    public static String checkUpdate(
            User user,
            String URL
    ) {
        Iterable<String> urlList;
        List<String> result = new ArrayList<>();

        if (URL.equals(null) || URL.length() == 0) {
            urlList = user.getOldTable().keySet();
        } else {
            urlList = Arrays.asList(URL.split(","));
        }
        for (String s : urlList) {
            if (!user.getOldTable().get(s).equals(user.getNewTable().get(s))) {
                result.add(s);
            }
        }
        if (result.size()==0) {
            return "Нет обновленных страниц";
        } else {
            return result.toString();
        }
    }

    public static Map<String, String> checkKeys(
            Set<String> oldKeysSet,
            Set<String> newKeysSet
    ) {
        Map<String, String> resultMap = new HashMap<>();
        List<String> dKeys = new ArrayList<>();
        StringBuilder sKeys = new StringBuilder();

        if (!oldKeysSet.equals(newKeysSet)) {

            for (String oldK : oldKeysSet) {
                if (!newKeysSet.contains(oldK)) {//was removed
                    dKeys.add(oldK);
                } else {
                    sKeys.append(oldK + ",");
                }
            }
            if (dKeys.isEmpty()) {
                resultMap.put("removed", "Нет удаленных страниц");
            } else {
                resultMap.put("removed", dKeys.toString());
                dKeys.clear();
            }
            for (String newK : newKeysSet) {
                if (!oldKeysSet.contains(newK)) {//was added
                    dKeys.add(newK);
                }
            }
            if (dKeys.isEmpty()) {
                resultMap.put("added", "Нет новых страниц");
            } else {
                resultMap.put("added", dKeys.toString());
                dKeys.clear();
            }
        } else {
            resultMap.put("removed", "Нет удаленных страниц");
            resultMap.put("added", "Нет новых страниц");
            return resultMap;
        }
        resultMap.put("reuse", sKeys.toString());
        return resultMap;
    }

    public static void checkUserDate(User user) {
        if (!user.getCheckDate().equals(LocalDate.now())) {
            user.getNewTable().clear();
        } else {
            user.getOldTable().clear();
            user.getOldTable().putAll(user.getNewTable());
            user.getNewTable().clear();
            user.setCheckDate(LocalDate.now());
        }
    }


    public static List<String> checkInput(String mesBody) {
        mesBody = mesBody.replace('\n', ' ').trim();
        List<String> list = Arrays.asList(Objects.requireNonNull(mesBody).split(","));
        List<String> list2 = new ArrayList<>();
        for (String s : list) {
            if (!s.equals(" ") & (s.contains(" ") || s.contains("\n"))) {
                s = s.trim();
                list2.add(s);
            } else {
                list2.add(s);
            }
        }
//        Jsoup.connect(s).get();
        return list2;
    }

    public static String getFrom(String mesHead) {
        if (!mesHead.equals(null)) {
            List<String> list2 = Arrays.asList(mesHead.split(" "));
            for (String s : list2) {
                if (s.contains("<") || s.contains(">")) {
                    mesHead = s;
                    break;
                }
            }
            mesHead = mesHead.replace(']', ' ')
                    .replace('<', ' ')
                    .replace('>', ' ')
                    .trim();
            return mesHead;
        } else {
            return null;
        }
    }
}
