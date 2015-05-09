package hu.uniobuda.nik.potatoe;

import java.util.ArrayList;
import java.util.List;

class Toolbar {

    private List<IPotato> listeners = new ArrayList<IPotato>();


    public void addListener(IPotato listener) {
        listeners.add(listener);
    }

    private void notifyListeners() {
        for (IPotato listener : listeners) {
            listener.onEndGame();
        }
    }

}
