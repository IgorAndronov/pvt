package com.pvt.dao.implementation.user;

import com.pvt.dao.interfaces.user.UserService;
import com.pvt.web.LoginController;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;



/**
 * Created by igor on 01.08.15.
 */
@Repository(value = "UserServiceImpl")
@Transactional
public class UserServiceImpl implements UserService {
    final static Logger logger = Logger.getLogger(UserServiceImpl.class);

    @Resource(name = "mySessionFactory")
    private SessionFactory sessionFactory;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public boolean isUserCanLogin(String login, String password, String userType, LoginController loginController) {
       return true;

    }

    @Override
    public String getIntroMessage(String clientType) {
//        String msg ="Hey. I'm your personal trainer Pee Vee Tee. Now your form is my concern. We will compile an individual program for you and bring your workouts to a new level.\n" +
//                "A little about me: I have the ability to develop and improve. This means that the more you and I co-ordinate, the more individual I become.\n" +
//                "So, let's begin. In order to make a plan for our lessons with you, I need to learn a little about you.\n" +
//                "1. What is your height and weight?\n" +
//                "2. Are there any physical limitations or contraindications?\n" +
//                "3. What kind of lifestyle do you lead, active with regular physical activity, moderate with periodic occupations or more passive, work or study without physical stress and lack of additional trainings.\n" +
//                "4. Finally, what are the goals of the coaches? This can be the development of strength, endurance, dumping or weight gain, simply bringing the muscles into tone and improving the body's performance.\n" +
//                "\n" +
//                "Well with the tasks we have decided and now we can start our trainings. I want to immediately warn that the plan of our classes will be constantly adjusted taking into account the course of our studies in order to achieve optimal results.";
//
        String msg="Привет.# Я твой персональный тренер.# Теперь твоя форма это моя забота.# Мы с тобой составим индивидуальную для тебя программу и выведем твои тренировки на новый уровень.#@@#\n" +
                "Немного обо мне:# я имею способность к развитию и совершенствованию.# Это значит что чем больше мы с тобой комуницируем, тем более индивидуальным я становлюсь.#@@# \n" +
                "И так начнем.# Для того чтобы составить план наших с тобой занятий мне необходимо немного узнать о тебе.##@@#\n" +
                "1. Какой у тебя рост и вес?##@@#\n" +
                "2. Есть ли какие-то физические ограничения или противопоказания?##@@#\n" +
                "3. Какой образ жизни ты ведешь?# Aктивный с регулярными физическими нагрузками,# умеренный с периодическими занятиями# или более пассивный-# работа или учеба без физ нагрузки и отсутствие дополнительных тренеровок?#@@#\n" +
                "4. И наконец какие цели тренеровок?# Это могут быть развитие силы,# выносливости,# сброс или набор веса,# просто приведение мышц в тонус и улучшение работы организма.####@@#\n" +
                "\n" +
                "Хорошо с задачами мы определились и теперь можем начать наши тренеровки.# Хочу сразу предупредить, что план наших занятий будет постоянно корректироваться с учетом хода наших занятий, чтобы достигнуть оптимальных результатов.#@@#";
         return msg;
    }
}
