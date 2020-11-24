package bgu.spl.mics.application.passiveObjects;

/**
 * Passive data-object representing a forest creature summoned when HanSolo and C3PO receive AttackEvents.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add fields and methods to this class as you see fit (including public methods).
 */
public class Ewok {
	int serialNumber;
	boolean available;

	public Ewok(int serialNumber) {
	    this.serialNumber = serialNumber;
	    available = true;
    }
  
    /**
     * Acquires an Ewok
     * @PRE: available == true
     * @POST: available == false
     */
    public void acquire() {
		if (isAvailable()){
		    changeAvailable();
        }
    }

    /**
     * release an Ewok
     * @PRE: available == false
     * @POST: available == true
     */
    public void release() {
    	if (!isAvailable()){
    	    changeAvailable();
        }
    }


    public boolean isAvailable() {  //todo: maybe make this private
        return this.available;
    }

    /**
     * @POST: available != @PRE(available)
     */
    public void changeAvailable() { //todo: maybe make this private
        this.available = !this.available;
    }
}
