import RaidsUnlimitedClient from "../api/raidsUnlimitedClient";
import Header from '../components/header';
import BindingClass from '../util/bindingClass';
import DataStore from '../util/DataStore';

/**
 * Logic needed for the view raid page of the website.
 */
class Homepage extends BindingClass {
    constructor() {
        super();

        // this.bindClassMethods(['mount'], this);

        // Create a enw datastore with an initial "empty" state.
        // this.dataStore = new DataStore(EMPTY_DATASTORE_STATE);
        // this.header = new Header(this.dataStore);
        this.header = new Header();
        // this.dataStore.addChangeListener(this.displaySearchResults);
        console.log("Homepage constructor");
    }

    mount() {
        this.header.addHeaderToPage();
    }
}


const main = async () => {
    const homePage = new Homepage()
    homePage.mount();
};

window.addEventListener('DOMContentLoaded', main);