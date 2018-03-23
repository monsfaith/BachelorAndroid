package running.java.mendelu.cz.bakalarskapraca;

/**
 * Created by Monika on 23.03.2018.
 */

public class Quote {

    private String text;
    private String author;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }


    public Quote(String text, String author){
        this.text = text;
        this.author = author;
    }

}
