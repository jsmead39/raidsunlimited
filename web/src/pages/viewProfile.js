import RaidsUnlimitedClient from "../api/raidsUnlimitedClient";
import Header from '../components/header';
import BindingClass from "../util/bindingClass";
import DataStore from "../util/DataStore";

/**
 * Logic needed for the view profile page of the website.
 */
class ViewProfile extends BindingClass {
    constructor() {
        super();
        this.bindClassMethods(['loadProfile', 'mount', 'loadRaidHistory'], this);
        this.dataStore = new DataStore();
        this.header = new Header(this.dataStore);
    }


    /**
     * Add the header to the page and load the RaidClient
     */
    mount() {
        this.header.addHeaderToPage();
        this.client = new RaidsUnlimitedClient();
        this.loadProfile();
        this.loadRaidHistory();
    }

    async loadProfile() {

            const urlParams = new URLSearchParams(window.location.search);
            const userId = urlParams.get('id');

            const response = await this.client.getProfile(userId);
            const logsLink = response.profileModel.logs;

            document.getElementById('displayName').innerText = response.profileModel.displayName;
            document.getElementById('warcraftLink').href = logsLink;
            document.getElementById('warcraftLink').innerText = logsLink;
            // document.getElementById('warcraftLogsLink').innerText = "Link to Warcraft Logs";


            //need to create a table here if it doesn't exist
            let charactersTable = document.getElementById('characterTable');
            if (!charactersTable) {
                charactersTable = document.createElement('table');
                charactersTable.setAttribute('id', 'characterTable');

                // noinspection DuplicatedCode
                const thead = document.createElement('thead');
                const headerRow = document.createElement('tr');
                ['Name', 'Class', 'Specialization', 'Role'].forEach(header => {
                    const th = document.createElement('th');
                    th.textContent = header;
                    headerRow.appendChild(th);
                });
                thead.appendChild(headerRow);
                charactersTable.appendChild(thead)
                document.getElementById('characterList').appendChild(charactersTable);

            }

            const tbody = charactersTable.querySelector('tbody') ||
                charactersTable.appendChild(document.createElement('tbody'));
            console.log(response);
            response.profileModel.characterList.forEach(character => {
                const dataRow = document.createElement('tr');
                [character.charName, character.charClass, character.specialization, character.role].forEach(value => {
                    const td = document.createElement('td');
                    td.textContent = value;
                    dataRow.appendChild(td);
                });
                tbody.appendChild(dataRow);
            });

    }

    async loadRaidHistory() {
        const urlParams = new URLSearchParams(window.location.search);
        const userId = urlParams.get('id');

        const raids = await this.client.getRaidHistory(userId);
        if(raids) {
            this.populateRaidList(raids);
        }
    }

    populateRaidList(raids) {
        const raidListElement = document.getElementById('raids');

        if (raids.raidModelList.length === 0) {
            raidListElement.innerText = 'No raids found';
        } else {
            // Create a table
            const table = document.createElement('table');

            // Create table header
            const thead = document.createElement('thead');
            const headerRow = document.createElement('tr');
            ['Raid ID', 'Raid Name', 'Server', 'Date', 'Raid Status'].forEach(header => {
                const th = document.createElement('th');
                th.textContent = header;
                headerRow.appendChild(th);
            });
            thead.appendChild(headerRow);
            table.appendChild(thead);

            // Create table body
            const tbody = document.createElement('tbody');

            raids.raidModelList.forEach(raid => {
                const tr = document.createElement('tr');

                // Add raidId as a hyperlink
                const td1 = document.createElement('td');
                const a = document.createElement('a');
                a.innerText = raid.raidId;
                a.href = `viewRaid.html?id=${raid.raidId}`;
                td1.appendChild(a);
                tr.appendChild(td1);

                // Add raidName, raidServer, raidDate
                [raid.raidName, raid.raidServer, raid.raidDate, raid.raidStatus].forEach(value => {
                    const td = document.createElement('td');
                    td.textContent = value;
                    tr.appendChild(td);
                });

                tbody.appendChild(tr);
            });

            table.appendChild(tbody);
            raidListElement.appendChild(table);
        }
    }
}

/**
 *
 * Main method to run when the page contents have loaded.
 */
const main = async() => {
    const userProfile = new ViewProfile();
    userProfile.mount();
};
window.addEventListener('DOMContentLoaded', main);




