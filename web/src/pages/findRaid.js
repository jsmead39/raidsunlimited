import RaidsUnlimitedClient from "../api/raidsUnlimitedClient";
import Header from '../components/header';
import BindingClass from "../util/bindingClass";
import DataStore from "../util/DataStore";

/**
 * Logic needed for the find raid page of the website.
 */
class FindRaid extends BindingClass {
    constructor() {
        super();
        this.bindClassMethods(['clientLoaded', 'mount', 'displayRaids'], this);
        this.dataStore = new DataStore();
        this.header = new Header(this.dataStore);
    }

    /**
     * Once the client is loaded, get the raids list metadata.
     */
    async clientLoaded() {
        const raids = await this.client.getAllRaids();
        console.log("raids in client loaded", raids);
        this.dataStore.set('raids', raids);
        this.displayRaids(raids);

    }

    /**
     * Add the header to the page and load the RaidClient
     */
    mount() {
        this.header.addHeaderToPage();

        this.client = new RaidsUnlimitedClient();
        this.clientLoaded();

    }

    displayRaids(raids) {
        const tableBody = document.getElementById('raid-table-body');
        tableBody.innerHTML = '';
        console.log("raids in displayRaids", raids);
        raids.raidModelList.forEach(raid => {
            console.log(raid);
            const row = document.createElement('tr');

            const raidNameCell = document.createElement('td');
            raidNameCell.innerHTML = `<a href="viewRaid.html?id=${raid.raidId}">${raid.raidName}</a>`;
            row.appendChild(raidNameCell);

            const serverCell = document.createElement('td');
            serverCell.innerHTML = raid.raidServer;
            row.appendChild(serverCell);

            const raidDateCell = document.createElement('td');
            raidDateCell.innerHTML = raid.raidDate;
            row.appendChild(raidDateCell);

            const raidTimeCell = document.createElement('td');
            raidTimeCell.innerHTML = raid.time;
            row.appendChild(raidTimeCell);

            const raidObjectiveCell = document.createElement('td');
            raidObjectiveCell.innerHTML = raid.raidObjective;
            row.appendChild(raidObjectiveCell);

            const lootDistributionCell = document.createElement('td');
            lootDistributionCell.innerHTML = raid.lootDistribution;
            row.appendChild(lootDistributionCell);

            const raidStatusCell = document.createElement('td');
            raidStatusCell.innerHTML = raid.raidStatus;
            row.appendChild(raidStatusCell);

            tableBody.appendChild(row);
        });
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





