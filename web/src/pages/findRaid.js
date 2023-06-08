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
        this.bindClassMethods(['clientLoaded', 'mount', 'displayRaids', 'filterTable'], this);
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

    /**
     * Provides the raid data into a table and adds filter functionality to the table.
     *
     * For each raid, a new table row is created and for each property of the raid a new cell is appended to the row.
     * The row is then added to the table body.
     *
     * Input elements are added to each table header, and an input event listener is attached.
     * The event listener calls the 'filterTable' method, which filters the table based on the input.
     * @param raids An object representing the list of raidModels.
     */
    displayRaids(raids) {
        const tableBody = document.getElementById('raid-table-body');
        tableBody.innerHTML = '';

        const headerRow = document.getElementById('raid-table-headers');

        //get the header row from the thead element
        Array.from(headerRow.getElementsByTagName('th')).forEach(headerCell => {
            headerCell.innerHTML += ' <input type="text">';
        });

        //add an event listener to the header inputs
        Array.from(headerRow.getElementsByTagName('input')).forEach(input => {
            input.addEventListener('input', this.filterTable);
        });

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

    /**
     * Filter the raids table based on the values of the header inputs.
     */
    filterTable() {
        //get rows from the table body
        const rows = Array.from(document.getElementById('raid-table-body')
            .getElementsByTagName('tr'));

        //get the header row
        const headerRow = document.getElementById('raid-table-headers')
            .getElementsByTagName('tr')[0];

        //get the values of the header inputs
        const filterValues = Array.from(headerRow.getElementsByTagName('input'))
            .map(input => input.value.toLowerCase());

        //show or hide each row depending on whether it matches the filter value
        rows.forEach(row => {
            const cells = Array.from(row.getElementsByTagName('td'));
            const rowValues = cells.map(cell => cell.textContent.toLowerCase());
            const matches = filterValues.every((filterValue, i) => !filterValue || rowValues[i].includes(filterValue));
            row.style.display = matches ? '' : 'none';
        })
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





