package fr.petitl.antichamber.timer;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import fr.petitl.antichamber.triggers.TriggerInfo;

public class LiveSplitControl implements TimerControl {

    protected Socket socket;
    protected OutputStreamWriter osw;
    
    public LiveSplitControl() {
	this("127.0.0.1", 16834);
    }
    
    public LiveSplitControl(String hostname, int port) {
	try {
	    socket = new Socket(hostname, port);
	} catch (UnknownHostException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	try {
	    osw = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }
    
    @Override
    public void exit() {
	reset();
    }

    @Override
    public void buildAndInjectRun(String name, List<TriggerInfo> splits) {
	reset();
    }

    @Override
    public void split(long stopTime) {
	send("split");
    }

    @Override
    public void start(long stopTime) {
	reset();
	send("startorsplit");
    }

    @Override
    public void reset() {
	send("reset");
    }
    
    protected void send(String str) {
	str += "\r\n";
	try {
	    osw.write(str, 0, str.length());
	    osw.flush();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

}
