import java.io.File;
import java.net.URL;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

public class SongWAV implements Runnable{
	private int mode =0;
	private int auIndex=0;
	private SourceDataLine auline[] = new SourceDataLine[10];
	private boolean kill;
	private boolean ready=true;
	private Thread wavThread= new Thread();
	private String filename ="";

	public boolean isReady() {
		return ready;
	}
	
	public SongWAV(int mode){
		this.mode=mode;
	}
	public void run(){
		if (mode==1){
			ready=false;
			kill=false;
			this.open(filename,auIndex);
		}
		else {
			while(true){
			ready=false;
			kill=false;
				if(mode==2){
				this.open(filename,auIndex);
				}
				if(kill) break;
			}
		}
	}
  public void open(String filename,int auIndex) {
    int EXTERNAL_BUFFER_SIZE = 524288;

    URL soundFile = this.getClass().getClassLoader()
        .getResource("Sound/"+filename);

    AudioInputStream audioInputStream = null;
    try {
      audioInputStream = AudioSystem.getAudioInputStream(soundFile);
    } catch (Exception e) {
      e.printStackTrace();
      return;
    }

    AudioFormat format = audioInputStream.getFormat();

    // Describe a desired line
    DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);




    try {
      auline[auIndex] = (SourceDataLine) AudioSystem.getLine(info);

      // Opens the line with the specified format,
      // causing the line to acquire any required
      // system resources and become operational.
      auline[auIndex].open(format);
    } catch (Exception e) {
      e.printStackTrace();
      return;
    }

    // Allows a line to engage in data I/O
    auline[auIndex].flush();
    auline[auIndex].start();

	
    int nBytesRead = 0;
    byte[] abData = new byte[EXTERNAL_BUFFER_SIZE];
    
    try {
      while (nBytesRead != -1) {
        nBytesRead = audioInputStream.read(abData, 0, abData.length);
        if (nBytesRead >= 0) {
          // Writes audio data to the mixer via this source data line
          // NOTE : A mixer is an audio device with one or more lines
          auline[auIndex].write(abData, 0, nBytesRead);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      return;
    } 
    finally {
      // Drains queued data from the line
      // by continuing data I/O until the
      // data line's internal buffer has been emptied
      auline[auIndex].drain();

      // Closes the line, indicating that any system
      // resources in use by the line can be released
      auline[auIndex].close();
    }

  }

public void stopMusic() {
	// TODO Auto-generated method stub
    if(wavThread.isAlive()){
    	wavThread.interrupt();
    }
    for(int i = 0 ; i < auline.length;i++){
    	if(auline[i]!=null)auline[i].stop();
    }

	kill=true;
	ready=true;
}

// getters and setters
public void setReady(boolean ready) {
	this.ready = ready;
}

public int getMode() {
	return mode;
}
public void setMode(int mode) {
	this.mode = mode;
}

public boolean isKill() {
	return kill;
}
public void setKill(boolean kill) {
	this.kill = kill;
}

public void openSong(String string) {
	// TODO Auto-generated method stub
	while(true){
    if(ready){
	wavThread = new Thread(this);
	this.filename= string;
	wavThread.start();
	break;
	}
	}
	auIndex++;
	if(auIndex==auline.length)auIndex=0;
}

public String getFilename() {
	return filename;
}

public void setFilename(String filename) {
	this.filename = filename;
}

public void openEffect(String string) {
	// TODO Auto-generated method stub

	this.filename= string;
	new Thread(this).start();
	auIndex++;
	if(auIndex==auline.length)auIndex=0;
	}
}

