import RaidsUnlimitedClient from "../api/raidsUnlimitedClient";
import Header from '../components/header';
import BindingClass from '../util/bindingClass';
import DataStore from '../util/DataStore';


/**
 * Logic needed for the create raid page of the website.
 */
class CreateRaid extends BindingClass {
    constructor() {
        super();
        this.bindClassMethods(['mount', 'submit', 'redirectToViewRaid'], this);
        this.dataStore = new DataStore();
        this.dataStore.addChangeListener(this.redirectToViewPlaylist);
        this.header = new Header(this.dataStore);
    }

        /**
         * Add the header to the page and load the MusicPlaylistClient.
         */
    mount() {
        document.getElementById('create').addEventListener('click', this.submit);

        this.header.addHeaderToPage();

        this.client = new RaidsUnlimitedClient();
    }

        /**
         * Method to run when the create raid submit button is pressed. Call the RaidEventService to create the
         * raid.
         */
    async submit(evt) {
        evt.preventDefault();

        const errorMessageDisplay = document.getElementById('error-message');
        errorMessageDisplay.innerText = ``;
        errorMessageDisplay.classList.add('hidden');

        const createButton = document.getElementById('create');
        const origButtonText = createButton.innerText;
        createButton.innerText = 'Loading...';

        const raidName = document.getElementById('raid-name').value;
        const raidServer = document.getElementById('raid-server').value;
        const raidDate = document.getElementById('raid-date').value;
        const time = document.getElementById('time').value;
        const raidSize = document.getElementById('raid-size').value;
        const raidObjective = document.getElementById('raid-objective').value;
        const lootDistribution = document.getElementById('loot-distribution').value;
        const dps = document.getElementById('dps-input').value;
        const healer = document.getElementById('healer-input').value;
        const tank = document.getElementById('tank-input');

        const requiredRoles = {
            DPS: dps,
            Healer: healer,
            Tank: tank
        };
        const raid = await this.client.createRaid(raidName, raidServer, raidDate, time, raidSize, raidObjective,
            lootDistribution, requiredRoles, (error) => {
                createButton.innerText = origButtonText;
                errorMessageDisplay.innerText = `Error: ${error.message}`;
                errorMessageDisplay.classList.remove('hidden');
            });
        this.dataStore.set('raid', raid);
    }

    /**
     * When the raidEvent is updated in the datastore, redirect to the view raid page.
     */
    redirectToViewRaid() {
        const raid = this.dataStore.get('raid');
        if (raid != null) {
            window.location.href = `/raid.html?id=${raid.id}`;
        }
    }
}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const createRaid = new CreateRaid();
    createRaid.mount();
};

window.addEventListener('DOMContentLoaded', main);