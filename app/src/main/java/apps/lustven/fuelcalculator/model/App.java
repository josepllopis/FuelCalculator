package apps.lustven.fuelcalculator.model;

public class App {
    String image,title,url;


    public App(){

    }

    public App(String image, String title, String url) {
        this.image = image;
        this.title = title;
        this.url = url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
