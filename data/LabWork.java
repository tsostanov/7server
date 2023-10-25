package data;

import exceptions.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Locale;

/**
 * LabWork objects are contained in the collection.
 */
public class LabWork implements Comparable<LabWork>, Serializable{

    private static final long serialVersionUID = 1L;

    public LabWork(){
        this.creationDate = LocalDateTime.now();
    }

    private Integer id; //�������� ���� ������ ���� ������ 0,
    // �������� ����� ���� ������ ���� ����������,
    // �������� ����� ���� ������ �������������� �������������.
    private String name; //���� �� ����� ���� null, ������ �� ����� ���� ������
    private Coordinates coordinates = new Coordinates(); //���� �� ����� ���� null
    private java.time.LocalDateTime creationDate; //���� �� ����� ���� null,
    // �������� ����� ���� ������ �������������� �������������
    private Double minimalPoint; //���� �� ����� ���� null, �������� ���� ������ ���� ������ 0
    private Integer tunedInWorks; //���� ����� ���� null
    private Difficulty difficulty; //���� �� ����� ���� null
    private Person author = new Person(); //���� ����� ���� null
    private User user;

    public void setId(Integer id) {
        this.id = id;
    }
    public void setName(String str) throws WrongInputException{
        if (str == null || str.isBlank()){
            throw new EmptyFieldException();
        }
        if(str.contains(",")){
            throw new ShouldNotContainException(",");
        }
        if (str.length()>128) {
            str = str.substring(0, 128);
        }
        this.name = str;
    }

    public void setCoordinatesX(String str) throws WrongInputException, NumberFormatException{
        if (str == null || str.isBlank()){
            throw new EmptyFieldException();
        }
        Float digit = Float.valueOf(str);
        if (digit >= 623) {
            throw new MustBeLowerException(623);
        }
        this.coordinates.setX(digit);
    }

    public void setCoordinatesY(String str) throws WrongInputException, NumberFormatException{
        if (str == null || str.isBlank()){
            throw new EmptyFieldException();
        }
        Long digit = Long.valueOf(str);
        if (digit <= -572) {
            throw new MustBeHigherException(-572);
        }
        this.coordinates.setY(digit);
    }

    public void setCreationDate(String str) throws WrongInputException {
        if (str == null || str.isBlank()){
            throw new EmptyFieldException();
        }
        LocalDateTime localDateTime = LocalDateTime.parse(str);
        this.creationDate = localDateTime;
    }
    public LocalDateTime getCreationDate(){
        return creationDate;
    }

    public void setMinimalPoint(String str) throws WrongInputException, NumberFormatException {
        if (str == null || str.isBlank()) {
            throw new EmptyFieldException();
        }
        Double digit = Double.valueOf(str);
        if (digit <= 0) {
            throw new MustBeHigherException(0);
        }
        this.minimalPoint = digit;
    }

    public void setTunedInWorks(String str) throws NumberFormatException {
        if (str == null || str.isBlank()) {
            this.tunedInWorks = 0;
            return;
        }
        Integer digit = Integer.valueOf(str);
        this.tunedInWorks = digit;
    }

    public void setDifficulty(String str) throws WrongInputException, IllegalArgumentException {
        if (str == null || str.isBlank()) {
            throw new EmptyFieldException();
        }
        str = str.toUpperCase(Locale.ROOT);
        switch (str) {
            case "1" -> str = "VERY_HARD";
            case "2" -> str = "INSANE";
            case "3" -> str = "TERRIBLE";
        }
        this.difficulty = Difficulty.valueOf(str);
    }

    public void setPersonName(String str) throws WrongInputException {
        if (str == null || str.isBlank()) {
            throw new EmptyFieldException();
        }
        this.author.setName(str);
    }

    public void setPersonHeight(String str) throws WrongInputException {
        if (str == null || str.isBlank()) {
            throw new EmptyFieldException();
        }
        long digit = Long.parseLong(str);
        if (digit <= 0) {
            throw new MustBeHigherException(0);
        }
        this.author.setHeight(digit);
    }

    public void setPersonEyeColor(String str) throws WrongInputException {
        if (str == null || str.isBlank()) {
            this.author.setEyeColor(Color.valueOf("BLACK"));
            return;
        }
        str = str.toUpperCase(Locale.ROOT);
        switch (str) {
            case "1" -> str = "RED";
            case "2" -> str = "BLACK";
            case "3" -> str = "YELLOW";
            case "4" -> str = "BLUE";
            case "5" -> str = "ORANGE";
            case "6" -> str = "WHITE";
        }
        this.author.setEyeColor(Color.valueOf(str));
    }

    public void setPersonHairColor(String str) throws WrongInputException {
        if (str == null || str.isBlank()) {
            throw new EmptyFieldException();
        }
        str = str.toUpperCase(Locale.ROOT);
        switch (str) {
            case "1" -> str = "RED";
            case "2" -> str = "BLACK";
            case "3" -> str = "YELLOW";
            case "4" -> str = "BLUE";
            case "5" -> str = "ORANGE";
            case "6" -> str = "WHITE";
        }
        this.author.setHairColor(Color.valueOf(str));
    }

    public void setPersonNationality(String str) throws WrongInputException {
        if (str == null || str.isBlank()) {
            this.author.setNationality(Country.valueOf("RUSSIA"));
            return;
        }
        str = str.toUpperCase(Locale.ROOT);
        switch (str) {
            case "1" -> str = "RUSSIA";
            case "2" -> str = "INDIA";
            case "3" -> str = "NORTH_KOREA";
        }
        this.author.setNationality(Country.valueOf(str));
    }

    public void setPersonLocationX(String str) throws WrongInputException {
        if (str == null || str.isBlank()) {
            throw new EmptyFieldException();
        }
        this.author.setPersonLocationX(str);
    }

    public void setPersonLocationY(String str) throws WrongInputException {
        if (str == null || str.isBlank()) {
            throw new EmptyFieldException();
        }
        this.author.setPersonLocationY(str);
    }

    public void setPersonLocationName(String str) throws WrongInputException {
        if (str == null || str.isBlank()) {
            throw new EmptyFieldException();
        }
        this.author.setPersonLocationName(str);
    }

    public void setUser(User user){
        this.user = user;
    }

    public void updateInfoFromElement(LabWork labWork) {
        this.name = labWork.name;
        this.coordinates = labWork.coordinates;
        this.minimalPoint = labWork.minimalPoint;
        this.tunedInWorks = labWork.tunedInWorks;
        this.difficulty = labWork.difficulty;
        this.author = labWork.author;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    public Double getMinimalPoint() {
        return minimalPoint;
    }

    public User getUser() {
        return user;
    }

    public String[] toStringArray() {
        String[] str = {
                String.valueOf(id),
                String.valueOf(name),
                String.valueOf(creationDate),
                String.valueOf(coordinates.getX()),
                String.valueOf(coordinates.getY()),
                String.valueOf(minimalPoint),
                String.valueOf(tunedInWorks),
                String.valueOf(difficulty),
                String.valueOf(author.getName()),
                String.valueOf(author.getHeight()),
                String.valueOf(author.getEyeColor()),
                String.valueOf(author.getHairColor()),
                String.valueOf(author.getNationality()),
                String.valueOf(author.getLocation().getX()),
                String.valueOf(author.getLocation().getY()),
                String.valueOf(author.getLocation().getName())
        };
        return str;
    }
    public Integer getTinedInWork(){
        return this.tunedInWorks;
    }
    public Float getCoordinatesX(){
        return this.coordinates.getX();
    }
    public Long getCoordinatesY(){
        return this.coordinates.getY();
    }

    public String getDifficulty() {
        return difficulty.name();
    }
    public String getPersonName(){
        return this.author.getName();
    }
    public Long getPersonHeight(){
        return this.author.getHeight();
    }
    public String getPersonEyeColor(){
        return this.author.getEyeColor().name();
    }
    public String getPersonHairColor(){
        return this.author.getHairColor().name();
    }
    public String getPersonNationality(){
        return this.author.getNationality().name();
    }
    public Integer getPersonLocationX(){
        return this.author.getLocation().getX();
    }
    public Float getPersonLocationY(){
        return this.author.getLocation().getY();
    }
    public String getPersonLocationName(){
        return this.author.getLocation().getName();
    }
    @Override
    public int compareTo(LabWork o) {
        return this.creationDate.compareTo(o.creationDate);
    }
}

class Coordinates implements Serializable{
    private static final long serialVersionUID = 1L;
    private Float x; //������������ �������� ����: 622, ���� �� ����� ���� null
    private Long y; //�������� ���� ������ ���� ������ -572, ���� �� ����� ���� null

    public void setX(Float x) {
        this.x = x;
    }

    public void setY(Long y) {
        this.y = y;
    }

    public Float getX() {
        return x;
    }

    public Long getY() {
        return y;
    }
}

class Person implements Serializable{
    private static final long serialVersionUID = 1L;
    private String name; //���� �� ����� ���� null, ������ �� ����� ���� ������
    private Long height; //���� �� ����� ���� null, �������� ���� ������ ���� ������ 0
    private Color eyeColor; //���� ����� ���� null
    private Color hairColor; //���� �� ����� ���� null
    private Country nationality; //���� ����� ���� null
    private Location location = new Location(); //���� �� ����� ���� null

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setHeight(long height) {
        this.height = height;
    }

    public long getHeight() {
        return height;
    }

    public void setEyeColor(Color eyeColor) {
        this.eyeColor = eyeColor;
    }

    public Color getEyeColor() {
        return eyeColor;
    }

    public void setHairColor(Color hairColor) {
        this.hairColor = hairColor;
    }

    public Color getHairColor() {
        return hairColor;
    }

    public void setNationality(Country country) {
        this.nationality = country;
    }

    public Country getNationality() {
        return nationality;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public void setPersonLocationX(String str) throws WrongInputException {
        if (str == null || str.isBlank()) {
            throw new EmptyFieldException();
        }
        Integer x = Integer.valueOf(str);
        this.location.setX(x);
    }

    public void setPersonLocationY(String str) throws WrongInputException {
        if (str == null || str.isBlank()) {
            throw new EmptyFieldException();
        }
        Float y = Float.valueOf(str);
        this.location.setY(y);
    }

    public void setPersonLocationName(String str) {
        this.location.setName(str);
    }

    class Location implements Serializable{
        private static final long serialVersionUID = 1L;

        private Integer x;//���� �� ����� ���� null
        private float y;
        private String name;

        public void setX(Integer x) {
            this.x = x;
        }

        public Integer getX() {
            return x;
        }

        public void setY(float y) {
            this.y = y;
        }

        public float getY() {
            return y;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

}

enum Difficulty {
    VERY_HARD,
    INSANE,
    TERRIBLE;
}

enum Color {
    RED,
    BLACK,
    YELLOW,
    BLUE,
    ORANGE,
    WHITE;
}

enum Country {
    RUSSIA,
    INDIA,
    NORTH_KOREA;
}


