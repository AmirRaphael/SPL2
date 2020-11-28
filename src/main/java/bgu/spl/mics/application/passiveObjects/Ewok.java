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


	public Ewok(int serialNumber){
	    this.serialNumber = serialNumber;
	    this.available = true;
    }
  
    /**
     * Acquires an Ewok
     * @PRE: available == true.
     * @POST: available == false.
     *
     */
    public void acquire() {
        if (isAvailable()){
            changeAvailable();
        }
    }

    /**
     * release an Ewok
     * @PRE:
     *    available == false.
     * @POST:
     *    available == true.
     */
    public void release() {
        if (!isAvailable()){
            changeAvailable();
        }
    }

    /**
     * @return available
     */
    public boolean isAvailable() { // TODO check if can be private
        return available;
    }

    /**
     * @POST:
     *   available != @PRE(available)
     */
    public void changeAvailable() { // TODO check if can be private
        this.available = !this.available;
    }
}
