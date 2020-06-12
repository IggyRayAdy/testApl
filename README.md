Добрый день.

Перед запуском приложения необходимо выполнить:

1 Установить mySQL

    $sudo apt update
    $sudo apt sudo apt install mysql-server

    Создать базу данных: 
    название базы - 'testApl';
    имя пользователя - 'user';
    пароль - 'password'.
    
    или создать базу с своими параметрами и сменить данные в src/main/resources/hibernate.properties
    hibernate.connection.url=jdbc:mysql://${MYSQL_HOST:localhost}:3306/'название базы' 
    hibernate.connection.username='имя пользователя'
    hibernate.connection.password='пароль'
    
2. Настройка почты

    Нобходимо создать пароль приложения 
    https://support.google.com/accounts/answer/185833?p=InvalidSecondFactor
    
    сменить данные в src/main/java/MailUtil.java на Ваши
    username = "*****@gmail.com";
    (пароль приложения) password = "*****"; 

3. Запуск

    Для запуска проекта необходимо в терминале дериктории проекта прописать 

    mvn compile
    mvn exec:java -Dexec.mainClass="Main"

 Программа будет ожидать письмо с темой -'testApl'. Письмо должно содержать только список урлов. На почту отпавителя будет отправленно письмо с результатом.
