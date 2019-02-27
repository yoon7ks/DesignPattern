/**
 * reference
 *  - https://gmlwjd9405.github.io/2018/07/06/strategy-pattern.html
 */

/**
 * 문제점
 *  1. 기존 로봇의 공격과 이동방법을 수정하는 경우
 *      - 아톰이 날 수없고 오직 걷게하고 싶다면?
 *      - 태권브이를 날게 하려면?
 *      - 새로운 기능으로 변경하려고 기존 코드의 내용을 수정해야 하므로 OCP에 위배된다.
 *      - 또한 태권브이와 아톰의 move() 메서드의 내용이 중복된다. 이런 중복상황은 많은 문제를 야기하는 원인이 된다.
 *      - 만약 걷는 방식에 문제가 있거나 새로운 방식으로 수정하려면 모든 중복 코드를 일관성있게 변경해야한다.
 *  2. 새로운 로봇을 만들어 기존의 공격 또는 이동방법을 추가/수정하는 경우
 *      - 새로운 로봇인 선가드를 만들어 태권브이의 미사일 공격 기능을 추가하려면?
 *      - 태권브이와 선가드 클래스의 attack()메서드의 내용이 중복된다.
 *      - 현재 시스템의 캡슐화의 단위가 'Robot' 자체이므로 로봇을 추가하기는 매우 쉽다.
 *      - 그러나 새로운 로봇인 선가드에 기존의 공격 또는 이동방법을 추가하거나 변경하려고 하면 문제가 발생한다.
 *
 * 해결책
 *  1. 문제를 해결하기 위해서는 무엇이 변화되었는지 찾은 후에 이를 클래스로 캡슐화해야 한다.
 *      - 로봇예제에서 변화되면서 문제를 발생시키는 요인은 로봇의 이동 방식과 공격방식의 변화이다.
 *      - 이것을 캡슐화 하려면 외부에서 구체적인 이동,공격방식을 담은 구체적인 클래스들을 은닉해야한다.
 *      - 공격과 이동을 위한 인터페이스를 각각 만들고 이들을 실제 실현한 클래스를 만들어야한다.
 *      - 로봇 클래스가 이동과 공격기능을 이용하는 클라이언트 역할을 수행
 *      - 구체적인 이동,공격 방식이 MovingStrategy, AttackStrategy 인터페이스에 의해 캡슐화되어있다.
 *      - 이 인터페이스들이 일종의 방화벽 역할을 수행해 Robot클래스의 변경을 차단해준다.
 *      - 스트래티지패턴을 이용하면 새로운 기능의 추가(새로운 이동, 공격)가 기존의 코드에 영향을 미치지 못하게
 *      하므로 OCP를 만족하는 설계가 된다.
 *      - 이렇게 변경된 새로운 구조에서는 외부에서 로봇 객체의 이동, 공격 방식을 임의대로 바꾸도록 해주는
 *      setter메서드가 필요하다.
 *      - setMovingStrategy, setAttackStrategy
 *      - 이렇게 변경이 가능한 이유는 상속대신 '집약관계'를 이용했기 때문이다.
 * */

abstract class Robot {
    private String name;
    private AttackStrategy attackStrategy;
    private MovingStrategy movingStrategy;

    public Robot(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void attack() {
        attackStrategy.attack();
    }

    public void move() {
        movingStrategy.move();
    }

    // 추상 메서드
    //public abstract void attack();
    //public abstract void move();

    // 집약 관계, 전체 객체가 메모리에서 사라진다고 해도 부분 객체는 사라지지 않는다.
    public void setAttackStrategy(AttackStrategy attackStrategy) {
        this.attackStrategy = attackStrategy;
    }

    public void setMovingStrategy(MovingStrategy movingStrategy) {
        this.movingStrategy = movingStrategy;
    }

}

interface AttackStrategy {
    public void attack();
}

class MissileStrategy implements AttackStrategy {

    @Override
    public void attack() {
        System.out.println("미사일을 갖고있어요.");
    }
}

class PunchStrategy implements AttackStrategy {

    @Override
    public void attack() {
        System.out.println("강력한 펀치를 갖고있어요.");
    }
}

interface MovingStrategy {
    public void move();
}

class FlyingStrategy implements MovingStrategy {

    @Override
    public void move() {
        System.out.println("날 수 있어요.");
    }
}

class WalkingStrategy implements MovingStrategy {

    @Override
    public void move() {
        System.out.println("전 걸을 수 있어요. 날 수 없어요.");
    }
}

class TaekwonV extends Robot {

    public TaekwonV(String name) {
        super(name);
    }

    // 삭제
//    public void attack() {
//        System.out.println("I have Missile.");
//    }
//    public void move() {
//        System.out.println("I can only walk.");
//    }
}

class Atom extends Robot {

    public Atom(String name) {
        super(name);
    }
    // 삭제
//    public void attack() {
//        System.out.println("I have strong punch.");
//    }
//    public void move() {
//        System.out.println("I can fly.");
//    }
}

class Client {
    public static void main(String[] args) {
        Robot taekwonV = new TaekwonV("태권브이");
        Robot atom = new Atom("아톰");

        // -- 추가(수정)된 전략방법
        taekwonV.setMovingStrategy(new WalkingStrategy());
        taekwonV.setAttackStrategy(new MissileStrategy());
        atom.setMovingStrategy(new FlyingStrategy());
        atom.setAttackStrategy(new PunchStrategy());
        // -- end

        System.out.println("My name is " + taekwonV.getName());
        taekwonV.move();
        taekwonV.attack();

        System.out.println();
        System.out.println("My name is " + atom.getName());
        atom.move();
        atom.attack();
    }
}
