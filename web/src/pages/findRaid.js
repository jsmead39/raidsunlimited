import RaidsUnlimitedClient from "../api/raidsUnlimitedClient";
import Header from '../components/header';
import BindingClass from "../util/bindingClass";
import DataStore from "../util/DataStore";

/**
 * Logic needed for the find raid page of the website.
 */
class ViewRaid extends BindingClass {
    constructor() {
        super();
        this.bindClassMethods(['clientLoaded', 'mount'], this);
        this.dataStore = new DataStore();
        this.header = new Header(this.dataStore);
    }

    /**
     * Once the client is loaded, get the raid metadata.
     */
    async clientLoaded() {

    }

    /**
     * Add the header to the page and load the RaidClient
     */
    async mount() {
        this.header.addHeaderToPage();

        this.client = new RaidsUnlimitedClient();
        this.clientLoaded();


    }
}


/**
 *
 * Main method to run when the page contents have loaded.
 */
const main = async() => {
    const findRaid = new FindRaid();
    findRaid.mount();
};

window.addEventListener('DOMContentLoaded', main);





