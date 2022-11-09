import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int x = sc.nextInt();//주차위치의 x좌표
        int y = sc.nextInt();//주차위치의 y좌표

        position p = new position(x, y);

        //서비스 구역 설정 : 100*100
        try{
            if(p.x>100 | p.y>100) {
                Exception e = new Exception("서비스 구역 벗어남, 주차불가");
                throw e;
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
            System.exit(0);
        }

        //주차장려구역 5곳 설정
        ArrayList<position> parkLots = new ArrayList(5);
        Random rand = new Random();

        for(int i = 0; i<5; i++) {
            position parkable = new position(rand.nextInt(100), rand.nextInt(100));//주차구역 좌표설정
            parkable.parkEnc = true;

            double xd = Math.pow((p.x - parkable.x), 2);
            double yd = Math.pow((p.y - parkable.y), 2);
            double d = Math.sqrt(xd + yd); //주자구역으로부터 i번 주자장려구역까지의 거리
            double d1 = Math.round(d*100)/100.0;//소수점 둘째자리

            parkable.setDistance(d1);
            parkable.setName((i+1)+"번 주차장");
            parkable.setCashback(rand.nextInt(100));//실제로는 유동인구에 따라 캐쉬백 결정

            parkLots.add(parkable);
        }
        List<position> showD = parkLots.stream().sorted(Comparator.comparing(position::getDistance)).collect(Collectors.toList());
        System.out.println("거리순 정렬: ");
        for(int i = 0; i<5; i++) {
            System.out.println(showD.get(i).getSentence());
        }
        //distance 순으로 정렬


        List<position> showC = parkLots.stream().sorted(Comparator.comparing(position::getCachback).reversed()).collect(Collectors.toList());
        System.out.println();
        System.out.println("캐쉬백순 정렬: ");
        for(int i = 0; i<5; i++) {
            System.out.println(showC.get(i).getSentence());
        }
        //cashback 역순으로 정렬


    }
}
class position{
    int x;
    int y;
    boolean parkEnc = false;//주차장려구역인지 아닌지(메인함수에서 지정하는 구역 외에는 장려구역 아닌 것으로 설정)
    double distance;
    String name;
    int cashback;

    position(int x, int y){
        this.x = x;
        this.y = y;
    }
    public void setDistance (double d){
        this.distance = d;
    }
    public void setName(String s){
        this.name = s;
    }
    public void setCashback(int c){
        this.cashback = c;
    }
    public double getDistance(){
        return distance;
    }
    public String getName(){
        return name;
    }
    public Integer getCachback(){
        return cashback;
    }
    public String getSentence(){
        return this.name + ": 거리- "+ this.distance + ", "+ "캐쉬백- "+ this.cashback;
    }
}