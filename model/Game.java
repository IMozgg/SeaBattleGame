package SeaBattle.model;

import SeaBattle.view.IDrawing;
import SeaBattle.view.IGame;

public class Game implements IGame {
    //-----------------------Параметры для ИИ------------------
    private Point lastPositionShip;             //  Последняя позиция подбитого корабля
    private Point firstPositionShip;            //  Первая позиция подбитого корабля
    private int[] targetShip = {4, 3, 2, 1};    //  Массив с кол-вом кораблей, которые нужно уничтожить
    private int numberShoot;                    //  Номер выстрела
    private int selectDiagonal;                 //  Выбор диагонали атаки
    private Point mask0;
    private Point mask1;
    private boolean isContinueShoot;            //  Флаг говорящий о том, чтобы добить корабль
    private boolean isRandomShoot;              //  Флаг указывающий на начало рандомной стрельбы (когда остались однопалубные)
    private boolean isIIShot;                   //  Параметр стрельбы методом ИИ
    private boolean isSecondHit;                //  Второе попадание (параметр для добивания)
    private byte attitudeShoot;                 //  Куда стрелять?
    //-----------------------------------------------------------

    private static volatile Game instance;
    public Player[] players;
    private boolean settingsAutoSetShip;        // Расстановка кораблей: 1 - автоматически, 0 - вручную
    private Player activePlayer;                //  Игрок, чей ход
    private IDrawing drawing;
    private boolean isShowGraph;                //  Показывать графику?
    private int countAttemptShips;              //  Счетчик попыток создать корабль (для выхода из тупика)

    private Game(IDrawing drawing) {
        this.drawing = drawing;
        init();
        initPlayers();
    }

    private Game() {
        init();
        initPlayers();
    }

    //  Ленивый синглтон. Потокобезопасный и быстрый
    public static Game getInstance(IDrawing drawing) {
        Game localInstance = instance;
        if (localInstance == null) {
            synchronized (Game.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new Game(drawing);
                }
            }
        }
        return localInstance;
    }

    private void init() {
        System.out.println("Добро пожаловать в игру \'Морской бой\' ");

        // Настроим контроллер игры
        isRandomShoot = false;
        isContinueShoot = false;
        attitudeShoot = 1;
        isIIShot = true;
        mask0 = new Point();
        mask1 = new Point();
        numberShoot = 0;
        countAttemptShips = 0;

        // Включим автоматическую расстановку кораблей
        settingsAutoSetShip = true;
        //settingsAutoSetShip = false;

        //  Включим показ графики
        isShowGraph = true;
        //  Выключим показ графики
        //isShowGraph = false;
    }

    private void initPlayers() {
        players = new Player[2];
        System.out.print("Введите ник 1 игрока: ");
        players[0] = new Player();
        System.out.print("Введите ник 2 игрока: ");
        players[1] = new Player();

        //  Создадим игровые поля игроков
        players[0].createSea();
        players[1].createSea();
        if (settingsAutoSetShip) {
            while (true) {

                //  Обработаем исключительную ситуацию
                // бывает, что рандомные числа приведут к тупику, от чего разместить новый корабль просто физически негде
                //  Для этого будем считать кол-во попыток на создание одного корабля, если превысит N то значит
                //  создается исключение, которое обрабатывается переинициализацией игрока
                try {
                    setRandomShip(players[0]);
                    break;
                } catch (Exception e) {
                    //System.out.println(e);
                    countAttemptShips = 0;
                    initPlayers(players[0]);
                }
            }

            while (true) {
                //  Обработаем исключительную ситуацию
                // бывает, что рандомные числа приведут к тупику, от чего разместить новый корабль просто физически негде
                //  Для этого будем считать кол-во попыток на создание одного корабля, если превысит N то значит
                //  создается исключение, которое обрабатывается переинициализацией игрока
                try {
                    setShipPerelman(players[1]);
                    break;
                } catch (Exception e) {
                    //System.out.println(e);
                    countAttemptShips = 0;
                    initPlayers(players[1]);
                }
            }
        } else {
            //  Ввод вручную
            //  Генерируем создание кораблей для второго игрока
            players[0].createShip(new Point(4, 2), 1, 0);
            players[0].createShip(new Point(1, 5), 2, 1);
            players[0].createShip(new Point(0, 7), 3, 0);
            players[0].createShip(new Point(6, 5), 4, 1);

            //  Генерируем создание кораблей для второго игрока
            players[1].createShip(new Point(4, 4), 4, 1);
            players[1].createShip(new Point(2, 5), 3, 0);
            players[1].createShip(new Point(9, 7), 3, 0);
            players[1].createShip(new Point(9, 0), 2, 0);
            players[1].createShip(new Point(0, 8), 2, 0);
            players[1].createShip(new Point(3, 9), 2, 1);
            players[1].createShip(new Point(2, 2), 1, 0);
            players[1].createShip(new Point(5, 1), 1, 0);
            players[1].createShip(new Point(9, 3), 1, 0);
            players[1].createShip(new Point(6, 7), 1, 0);
            players[1].clearMaskShip();
        }
        System.out.println("Корабли созданы");

        //  Укажем кто первый из игроков ходит
        activePlayer = players[(int) (Math.random() * 2)];
    }

    //  Повторная инициалиазция (для перезаписи данных)
    private void initPlayers(Player curPlayer) {
        //  Создадим игровые поля игроков
        curPlayer.createSea();
        curPlayer.clearAllShips();
    }

    public int shootPlayer(Point p, Player player) {
        Point tempPoint = p;

        if (isShowGraph) {
            System.out.print("Выстрел по " + tempPoint + " - ");
            switch (player.shoot(tempPoint)) {
                case -1:
                    System.out.println("Мимо");
                    return -1;
                case 0:
                    System.out.println("Попадание");
                    return 0;
                case 1:
                    System.out.println("Убит");
                    return 1;
                case 2:
                    System.out.println("Уже стреляли");
                    return 2;
            }
        } else {
            return (player.shoot(tempPoint));
        }
        return -2;
    }

    public Player startAutoGame() {
        //  Автоматическая игра
        //  Играем, пока у одного из игроков не закончатся корабли
        while (players[0].getSea().getCountShip() > 0 & players[1].getSea().getCountShip() > 0) {
            for (int j = 0; j < 2; j++) {
                if (isShowGraph) {
                    System.out.println("Ходит игрок " + activePlayer.getName());
                }

                if (activePlayer == players[0]) {   // Активный игрок ходит против другого игрока
                    iiShoot(players[1]);
                } else {
                    simpleShoot(players[0]);
                }
                activePlayer.incCountShoot();

                if (isShowGraph) {
                    System.out.println("Поле игрока: " + players[0]);
                    drawing.draw(players[0]);
                    System.out.println("Поле игрока: " + players[1]);
                    drawing.draw(players[1]);
                }
                if (players[0].getSea().getCountShip() > 0 & players[1].getSea().getCountShip() > 0) {
                    changePlayer();
                } else {
                    break;
                }
            }
        }

        if (isShowGraph) {
            System.out.println("Игрок " + players[0]);
            System.out.println("Кол-во выстрелов игрока  - " + players[0].getCountShoot());
            System.out.println("Поле игрока: " + players[0]);
            drawing.draw(players[0]);

            System.out.println("Игрок " + players[1]);
            System.out.println("Кол-во выстрелов игрока - " + players[1].getCountShoot());
            System.out.println("Поле игрока: " + players[1]);

            drawing.draw(players[1]);
        } else {
            System.out.println("Игрок " + activePlayer);
            System.out.println("Кол-во выстрелов игрока - " + activePlayer.getCountShoot());
        }

        return activePlayer;
    }

    public void simpleShoot(Player player) {
        Point tempPoint = new Point();
        boolean isRepeat;

        do {
            tempPoint.setX((int) (Math.random() * 10));
            tempPoint.setY((int) (Math.random() * 10));

            //  Проверяем, что координата попадает в поле
            if ((tempPoint.getX() >= 0 & tempPoint.getX() < player.getSea().getSize()) & (tempPoint.getY() >= 0 & tempPoint.getY() < player.getSea().getSize())) {
                //  Проверяем, что игрок туда не стрелял и нет маски
                if (player.getSeaShoot()[tempPoint.getX()][tempPoint.getY()] == 0 & player.getMaskShipSea()[tempPoint.getX()][tempPoint.getY()] == 0) {
                    //  Если все выполнено, то повторять не надо
                    isRepeat = false;
                } else {
                    //  Если уже стреляли или маска, то повторим блок кода
                    isRepeat = true;
                }
            } else {
                //  Если координаты выходят за пределы, то повторяем код пока не будет найден разрешенный ваыстрел
                isRepeat = true;
            }
        } while (isRepeat);

        shootPlayer(tempPoint, player);
    }

    public void setRandomShip(Player player) throws Exception {
        //  4 - однопалубных        4 - 1
        //  3 - двухпалубных        3 - 2
        //  2 - трехпалубных        2 - 3
        //  1 - четырехпалубный     1 - 4
        int randomAttitude;
        int randomX;
        int randomY;
        Point newPoint;
        for (int i = 0; i < 4; i++) {
            for (int j = i; j < 4; j++) {
                newPoint = new Point();
                do {
                    if (i >= 2) {
                        countAttemptShips++;
                        if (countAttemptShips >= 100) {
                            throw new Exception("Нерешимая ситуация");
                        }
                    }
                    randomAttitude = (int) (Math.random() * 2);

                    // Рандомизация координат выполняется на основе направления корабля и его размера (чтобы не выйти за границы игрового поля)
                    if (randomAttitude == 0) {
                        randomX = (int) (Math.random() * 10);
                        randomY = (int) (Math.random() * (10 - i));
                    } else {
                        randomX = (int) (Math.random() * (10 - i));
                        randomY = (int) (Math.random() * 10);
                    }
                    newPoint.setX(randomX);
                    newPoint.setY(randomY);

                } while (checkShipBeside(newPoint, i + 1, randomAttitude, player));
                player.createShip(newPoint, i + 1, randomAttitude);
            }
        }
        //  После создания всех кораблей, нужно очистить маску
        player.clearMaskShip();
    }

    public void setShipPerelman(Player player) throws Exception {
        //  4 - однопалубных        4 - 1
        //  3 - двухпалубных        3 - 2
        //  2 - трехпалубных        2 - 3
        //  1 - четырехпалубных     1 - 4
        int randomAttitude;
        int randomX;
        int randomY;
        Point newPoint;

        //  Корабли от 2-х палубных до 4-х палубных
        newPoint = new Point();
        for (int i = 0; i < 4; i++) {
            for (int j = i; j < 4; j++) {
                do {
                    do {
                        if (i >= 2) {
                            countAttemptShips++;
                            if (countAttemptShips >= 100) {
                                throw new Exception("Нерешимая ситуация");
                            }
                        }
                        randomAttitude = (int) (Math.random() * 2);
                        // Рандомизация координат выполняется на основе направления корабля и его размера (чтобы не выйти за границы игрового поля)
                        if (randomAttitude == 0) {
                            randomX = (int) (Math.random() * 10);
                            randomY = (int) (Math.random() * (10 - i));
                        } else {
                            randomX = (int) (Math.random() * (10 - i));
                            randomY = (int) (Math.random() * 10);
                        }
                        newPoint.setX(randomX);
                        newPoint.setY(randomY);
                    }
                    while (checkFreeSpaceAboutShip(newPoint, i + 1, randomAttitude, player) > (i + 4));
                } while (checkShipBeside(newPoint, i + 1, randomAttitude, player));
                player.createShip(newPoint, i + 1, randomAttitude);
            }
        }

        //  После создания всех кораблей, нужно очистить маску
        player.clearMaskShip();
    }

    //  Проверка свободного места вокруг корабля
    private int checkFreeSpaceAboutShip(Point p, int size, int attitude, Player curPlayer) {
        byte[][] maskSea = curPlayer.getMaskShipSea();
        int x0;
        int x1;
        int y0;
        int y1;
        int sumFreeSpace = 0;

        //  Указываем границы перебора
        x0 = p.getX() - 1;
        y0 = p.getY() - 1;
        if (attitude == 0) {
            x1 = p.getX() + 1;
            y1 = p.getY() + size;
        } else {
            x1 = p.getX() + size;
            y1 = p.getY() + 1;
        }

        //  Проверяем сколько свободного места около корабля
        for (int i = x0; i <= x1; i++) {
            for (int j = y0; j <= y1; j++) {
                if ((i >= 0 & i < 10 & j >= 0 & j < 10) ^ (i >= x0 + 1 & i <= x1 - 1 & j >= y0 + 1 & j <= y1 - 1)) {
                    if (maskSea[j][i] == 0) {
                        sumFreeSpace++;
                    }
                }
            }
        }
        return sumFreeSpace;
    }

    //  Проверка общего свободного места
    private int countFreeSpace(Player p) {
        byte[][] tempMaskPlayer = p.getMaskShipSea();
        int countFreeSpace = 0;

        for (int i = 0; i < p.getSea().getSize(); i++) {
            for (int j = 0; j < p.getSea().getSize(); j++) {
                if (tempMaskPlayer[i][j] == 0) {
                    countFreeSpace++;
                }
            }
        }

        return countFreeSpace;
    }

    //  Проверка на разрешение расстановки корабля в выбранной точке
    private boolean checkShipBeside(Point p, int size, int attitude, Player curPlayer) {
        int lastX;
        int lastY;
        int firstX;
        int firstY;
        Point tempPoint;

        //  Получаем координаты последних точек вокруг корабля
        //  * * *   * * * *
        //  * О *   * О О *
        //  * О *   * * * *
        //  * * *
        if (attitude == 0) {
            if (p.getX() + 1 > 9) {
                lastX = 9;
            } else {
                lastX = p.getX() + 1;
            }
            if (p.getY() + size > 9) {
                lastY = 9;
            } else {
                lastY = p.getY() + size;
            }
        } else {
            if (p.getX() + size > 9) {
                lastX = 9;
            } else {
                lastX = p.getX() + size;
            }
            if (p.getY() + 1 > 9) {
                lastY = 9;
            } else {
                lastY = p.getY() + 1;
            }
        }

        //  Получаем начальные координаты проверки
        if (p.getX() == 0) {
            firstX = 0;
        } else {
            firstX = p.getX() - 1;
        }
        if (p.getY() == 0) {
            firstY = 0;
        } else {
            firstY = p.getY() - 1;
        }

        //  Проверяем есть ли в облаcти будущего корабля другие корабли
        tempPoint = new Point();
        for (int j = firstX; j <= lastX; j++) {
            for (int k = firstY; k <= lastY; k++) {
                tempPoint.setX(j);
                tempPoint.setY(k);

                if (curPlayer.getSea().getShipOnFields(tempPoint) != null) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isSettingsAutoSetShip() {
        return settingsAutoSetShip;
    }

    public void changeSettingsAutoSetShip() {
        this.settingsAutoSetShip = !this.settingsAutoSetShip;
    }

    public void changePlayer() {
        if (activePlayer == players[0]) {
            activePlayer = players[1];
        } else {
            activePlayer = players[0];
        }
    }

    public Player getActivePlayer() {
        return activePlayer;
    }

    private void finishOffShip(Player player) {
        Point tempPoint = new Point();
        int tempX = 0;
        int tempY = 0;

        boolean shootAccess = false;    //  Флаг говорящий о том, что выстрел можно проводить

        //  Блок решающий в какую клетку проводить выстрел, пока не определиться с клеткой будет выполняться
        while (!shootAccess) {
            switch (attitudeShoot) {
                case 1: // Стрельба вверх
                    tempX = lastPositionShip.getX();
                    tempY = lastPositionShip.getY() - 1;
                    break;
                case 2: //  Стрельба вправо
                    tempX = lastPositionShip.getX() + 1;
                    tempY = lastPositionShip.getY();
                    break;
                case 3: //  Стрельба вниз
                    tempX = lastPositionShip.getX();
                    tempY = lastPositionShip.getY() + 1;
                    break;
                case 4: //  Стрельба влево
                    tempX = lastPositionShip.getX() - 1;
                    tempY = lastPositionShip.getY();
                    break;
                default:
                    break;
            }

            if ((tempY >= 0 & tempY < player.getSea().getSize()) & (tempX >= 0 & tempX < player.getSea().getSize())) {
                if ((player.getMaskShipSea()[tempX][tempY] == 0) & (player.getSeaShoot()[tempX][tempY] == 0)) {
                    tempPoint.setX(tempX);
                    tempPoint.setY(tempY);
                    lastPositionShip = tempPoint;
                    shootAccess = true;
                } else {
                    attitudeShoot++;
                    lastPositionShip = firstPositionShip;
                    shootAccess = false;
                }
            } else {
                attitudeShoot++;
                lastPositionShip = firstPositionShip;
                shootAccess = false;
            }
        }

        switch (shootPlayer(tempPoint, player)) {
            //  Пападание в корабль
            case 0:
                isSecondHit = true;
                break;
            //  Потопление корабля
            case 1:
                isContinueShoot = false;

                //  Получим начальные и конечные координаты потопленного корабля
                mask0.copy(player.getSea().getShipOnFields(tempPoint).getShipOnCells()[0]);
                mask1.copy(player.getSea().getShipOnFields(tempPoint).getShipOnCells()[player.getSea().getShipOnFields(tempPoint).getSize() - 1]);

                //  Вернем координаты корбля к исходным домножив на -1
                mask0.multiplyXY(-1);
                mask1.multiplyXY(-1);

                //  Расширим диапозон маски на -1 и +1 соответственно
                mask0.deductXY(1);
                mask1.addXY(1);

                //  Применим маску
                player.applyMaskShip(mask0, mask1);

                //  Удалим корабль из списка кораблей для уничтожения
                int tempSizeSquare = getSizeSquare();
                targetShip[player.getSea().getShipOnFields(tempPoint).getSize() - 1]--;
                if (tempSizeSquare > getSizeSquare()) {
                    numberShoot = 0;
                }
                isSecondHit = false;
                break;
            default:
                if (isSecondHit) {
                    attitudeShoot = attitudeShotRotate180(attitudeShoot);
                } else {
                    attitudeShoot++;
                }
                lastPositionShip = firstPositionShip;
                break;
        }
    }

    public void iiShoot(Player player) {
        Point[] coordXY = new Point[2];
        Point pointShoot;
        int maxSize;
        int sizeSquare = 0; //  Размер квадрата
        int sizeSea = player.getSea().getSize();
        boolean isRepeat;

        //  Для рандомного боя, будут вычисляться, пока не будет пустое место на поле
        int tempX;
        int tempY;

        coordXY[0] = new Point();
        coordXY[1] = new Point();

        if (!isContinueShoot) {
            if (isRandomShoot) {
                do {
                    tempX = (int) (Math.random() * 10);
                    tempY = (int) (Math.random() * 10);
                } while (player.getMaskShipSea()[tempX][tempY] != 0 | player.getSeaShoot()[tempX][tempY] != 0);
                pointShoot = new Point(tempX, tempY);
            } else {
                sizeSquare = getSizeSquare();
                selectDiagonal = sizeSquare % 2;
                maxSize = (int) Math.ceil((float) sizeSea / sizeSquare) * sizeSquare;
                do {
                    if ((numberShoot - (int) Math.floor((float) numberShoot / maxSize) * maxSize) >= sizeSea) {
                        numberShoot += maxSize - sizeSea;
                    }

                    coordXY = getCoordSquare(numberShoot, sizeSquare, sizeSea);
                    pointShoot = getCoordShoot(numberShoot, coordXY, selectDiagonal);

                    //  Если маски нет и выстрела не было
                    numberShoot++;

                    //  Проверяем, что координата попадает в поле
                    if ((pointShoot.getX() >= 0 & pointShoot.getX() < sizeSea) & (pointShoot.getY() >= 0 & pointShoot.getY() < sizeSea)) {
                        //  Проверяем, что игрок туда не стрелял и нет маски
                        if (player.getSeaShoot()[pointShoot.getX()][pointShoot.getY()] == 0 & player.getMaskShipSea()[pointShoot.getX()][pointShoot.getY()] == 0) {
                            //  Если все выполнено, то повторять не надо
                            isRepeat = false;
                        } else {
                            //  Если уже стреляли или маска, то повторим блок кода
                            isRepeat = true;
                        }
                    } else {
                        //  Если координаты выходят за пределы, то повторяем код пока не будет найден разрешенный ваыстрел
                        isRepeat = true;
                    }
                } while (isRepeat);
            }

            //проводим выстрел
            if (isIIShot) {
                switch (shootPlayer(pointShoot, player)) {
                    //  Пападание в корабль
                    case 0:
                        isContinueShoot = true;
                        firstPositionShip = lastPositionShip = pointShoot;
                        break;
                    //  Потопление корабля
                    case 1:
                        //  Получим начальные и конечные координаты потопленного корабля
                        mask0.copy(player.getSea().getShipOnFields(pointShoot).getShipOnCells()[0]);
                        mask1.copy(player.getSea().getShipOnFields(pointShoot).getShipOnCells()[player.getSea().getShipOnFields(pointShoot).getSize() - 1]);

                        //  Вернем координаты корбля к исходным домножив на -1
                        mask0.multiplyXY(-1);
                        mask1.multiplyXY(-1);

                        //  Расширим диапозон маски на -1 и +1 соответственно
                        mask0.deductXY(1);
                        mask1.addXY(1);

                        //  Применим маску
                        player.applyMaskShip(mask0, mask1);

                        //  Удалим корабль из списка кораблей для уничтожения
                        targetShip[player.getSea().getShipOnFields(pointShoot).getSize() - 1]--;
                        break;
                    // Мимо и прочее
                    default:
                        break;
                }
                if (getSizeSquare() == 1) {
                    isRandomShoot = true;
                }
            }
        } else {
            finishOffShip(player);
        }
    }

    /**
     * Рассчет и получение координат квадрата для проведения стрельбы
     *
     * @param numberShoot номер выстрела ( начинается от нуля и заканчивается последним в поле ( зависит от размера кадрата )
     * @param sizeSquare  размер квадрата обстрела
     * @param sizeSea     размер игрового поля
     * @return
     */
    private Point[] getCoordSquare(int numberShoot, int sizeSquare, int sizeSea) {
        Point[] coordXY = new Point[2];
        coordXY[0] = new Point();
        coordXY[1] = new Point();

        // Рассчет номера квадрата
        int numbSquare = (int) Math.floor((float) numberShoot / sizeSquare);

        // Рассчет кол-ва квадратов на одной линии в поле
        int countSquareSeeLine = (int) Math.ceil((float) sizeSea / sizeSquare);

        // Номер линии для определенного номера квадрата
        int countLine = (int) Math.floor((float) numbSquare / countSquareSeeLine);

        //  Рассчет координат квадранта на основе рассчетных значений
        coordXY[0].setX((numbSquare - countLine * countSquareSeeLine) * sizeSquare);
        coordXY[1].setX(coordXY[0].getX() + sizeSquare - 1);
        coordXY[0].setY(countLine * sizeSquare);
        coordXY[1].setY(coordXY[0].getY() + sizeSquare - 1);

        return coordXY;
    }

    /**
     * Получение координат очередного выстрела (главная/побочная) диагонали
     *
     * @param numberShoot    - номер выстрела (общий)
     * @param coordXYSquare  - координаты выбранного квадрата для обстрела
     * @param selectDiagonal - номер диагонали (0 - главная, 1 - побочная)
     * @return
     */
    private Point getCoordShoot(int numberShoot, Point[] coordXYSquare, int selectDiagonal) {
        Point coordShoot = new Point();
        int copyNumberShoot = numberShoot;
        int calcSimpleXY;
        int sizeSquare;

        sizeSquare = coordXYSquare[1].getX() - coordXYSquare[0].getX() + 1;
        calcSimpleXY = copyNumberShoot - ((int) Math.floor((float) copyNumberShoot / sizeSquare) * sizeSquare);
        if (selectDiagonal == 0) {
            coordShoot.setX(calcSimpleXY + coordXYSquare[0].getX());
            coordShoot.setY(calcSimpleXY + coordXYSquare[0].getY());
        } else {
            coordShoot.setX(coordXYSquare[0].getX() + calcSimpleXY);
            coordShoot.setY(coordXYSquare[1].getY() - calcSimpleXY);
        }

        return coordShoot;
    }

    private int getSizeSquare() {
        for (int i = targetShip.length - 1; i >= 0; i--) {
            if (targetShip[i] != 0) {
                return i + 1;
            }
        }
        return -1;  //  Все цели уничтожены
    }

    private byte attitudeShotRotate180(byte attitudeShot) {
        byte aShoot;
        aShoot = attitudeShot;

        if (aShoot < 3) {
            return (byte) (aShoot + 2);
        } else {
            return (byte) (aShoot - 2);
        }
    }
}
