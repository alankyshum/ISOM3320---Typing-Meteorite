package meteorite;

public class Player {
  private int lv = 0;
  private String name;
  private int score = 0;
  
  public Player(){

  }
    
  public Player(int lv, String name, int score){
      this.lv = lv;
      this.name = name;
      this.score = score;
  }
  
  public void score_up (){
    score += GameSystem.SCOREINCRE;
  }

  public void lv_up(){
    if (this.score >= GameSystem.LV_UP_THRESHOLD*this.lv) {
      lv++;
    }
  }

  public void set_name(String name){
    this.name = name;
  }

  public int get_lv(){
    return this.lv;
  }

  public String get_name(){
    return this.name;
  }

  public int get_score(){
    return this.score;
  }
}