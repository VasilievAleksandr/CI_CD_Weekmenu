package by.weekmenu.api.utils;

import by.weekmenu.api.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SheduledTasks {

    @Autowired
    MenuRepository menuRepository;

    @Scheduled(cron = "0 01 00 01 01 ?")
    public void changeMenuNamesAtFirstDayOfTheYear() {
        String regex="\\d{2}\\D\\d{2}\\s\\D\\s\\d{2}\\D\\d{2}";
        try {
            menuRepository.findAllByIsActiveIsFalse().stream().forEach(menu -> {
                String menuCurrentName = menu.getName();
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(menuCurrentName);
                while(matcher.find()) {
                    menu.setName(menuCurrentName.substring(0, matcher.start())
                            .concat(" ")
                            .concat(WeekMenuDatesUtils.getWeekDates(menu.getWeekNumber()))
                            .concat(" ")
                            .concat(menu.getName().substring(matcher.end(), menuCurrentName.length())));
                    menuRepository.save(menu);
                }
            });
        }
        catch (Exception e) {
            // TODO create logging
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "0 01 00 ? * MON")
    public void changeMenuStatusAtFirstDayOfTheWeek() {
        try {
            int currentWeekNumber = WeekMenuDatesUtils.getCurrentWeekNumber();
            menuRepository.findAll().stream().forEach(menu -> {
                if (menu.getIsActive()){
                    menu.setIsActive(false);
                }
                if (menu.getWeekNumber()== currentWeekNumber){
                    menu.setIsActive(true);
                }
                menuRepository.save(menu);
            });
        }
        catch (Exception e) {
            // TODO create logging
            e.printStackTrace();
        }
    }
}
