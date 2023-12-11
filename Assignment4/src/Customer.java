import java.util.Date;

//Customer Class
class Customer{
    int id;
    String firstName;
    String lastName;
    double prevTotal;
    String prevDate;

    //Constructor
    public Customer(int idNum, String fName, String lName, double tot, String date){
        this.id = idNum;
        this.firstName = fName;
        this.lastName = lName;
        this.prevTotal = tot;
        this.prevDate = date;
    }

    //Getters
    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public double getPrevTotal() {
        return prevTotal;
    }

    public String getPrevDate() {
        return prevDate;
    }

    //Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPrevTotal(double prevTotal) {
        this.prevTotal = prevTotal;
    }

    public void setPrevDate(String prevDate) {
        this.prevDate = prevDate;
    }
}