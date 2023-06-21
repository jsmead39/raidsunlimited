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
        this.bindClassMethods(['mount', 'submit', 'redirectToViewRaid', 'clientLoaded', 'populateRaidData',
            'getFormValues', 'update'], this);
        this.dataStore = new DataStore();
        // this.dataStore.addChangeListener(this.redirectToViewRaid);
        this.header = new Header(this.dataStore);
    }

        /**
         * Add the header to the page and load the RaidClient.
         */
    mount() {
        this.header.addHeaderToPage();
        this.client = new RaidsUnlimitedClient();

        this.clientLoaded();

        document.getElementById('create').addEventListener('click', this.submit);


    }

    async clientLoaded() {
        try {
            const urlParams = new URLSearchParams(window.location.search);

            const raidId = urlParams.get('id');
            let raid;

            if (raidId) {
                raid = await this.client.getRaid(raidId);
                this.populateRaidData(raid);
                this.dataStore.set('raid', raid);
                const createButton = document.getElementById('create');
                createButton.innerText = 'Update Raid';
                createButton.removeEventListener('click', this.submit);
                createButton.addEventListener('click', this.update);
            }
            const cardElement = document.querySelector('.card.hidden');
            cardElement.classList.remove('hidden');
            cardElement.style.display = '';

        } catch (error) {
            console.error("Error loading the raid");
        }
    }

    populateRaidData(raid) {
        document.getElementById('formTitle').innerText = "Update Raid";
        document.getElementById('raid-id').value = raid.raidId;
        document.getElementById('raid-name').value = raid.raidName;
        document.getElementById('raid-server').value = raid.raidServer;
        document.getElementById('raid-date').value = raid.raidDate;
        document.getElementById('raid-time').value = raid.time;
        document.getElementById('raid-size').value = raid.raidSize;
        document.getElementById('raid-objective').value = raid.raidObjective;
        document.getElementById('loot-distribution').value = raid.lootDistribution;
        document.getElementById('dps-input').value = raid.requiredRoles.Dps;
        document.getElementById('healer-input').value = raid.requiredRoles.Healer;
        document.getElementById('tank-input').value = raid.requiredRoles.Tank;
        document.getElementById('raid-status').value = raid.raidStatus;

        const formGroupElement = document.querySelector('.form-group');
        formGroupElement.style.display = '';


        const raidStatusDropdown = document.getElementById('raid-status');
        raidStatusDropdown.value = raid.raidStatus;


    }


        /**
         * Method to run when the create raid submit button is pressed. Call the RaidEventService to create the
         * raid.
         */
    async submit(evt) {
        evt.preventDefault();

        const changeButton = document.getElementById('create');
        const origButtonText = changeButton.innerText;
        const messagePopup = document.getElementById('messagePopup');
        const messageText = document.getElementById('messageText');


        changeButton.innerText = 'Loading...';

            const {
                raidId,
                raidName,
                raidServer,
                raidDate,
                time,
                raidSize,
                raidObjective,
                lootDistribution,
                requiredRoles,
                raidStatus
            } = this.getFormValues();


            const raid = await this.client.createRaid(raidName, raidServer, raidDate, time, raidSize,
            raidObjective, lootDistribution, requiredRoles, (error) => {
                changeButton.innerText = origButtonText;
                messageText.innerText = `Error: ${error.message}`;
                messagePopup.classList.remove('hidden');
            });
        this.dataStore.set('raid', raid);

            setTimeout(() => {
                this.redirectToViewRaid();
            }, 5000);
    }

    async update(evt) {
        evt.preventDefault();

        const changeButton = document.getElementById('create');
        const origButtonText = changeButton.innerText;
        const messagePopup = document.getElementById('messagePopup');
        const messageText = document.getElementById('messageText');

        const {
            raidId,
            raidName,
            raidServer,
            raidDate,
            time,
            raidSize,
            raidObjective,
            lootDistribution,
            requiredRoles,
            raidStatus
        } = this.getFormValues();

        try {
            const response = await this.client.updateRaid(raidName, raidServer, raidDate, time, raidSize,
                raidObjective, lootDistribution, requiredRoles, raidStatus, raidId);

            if (response.status === 200) {
                messageText.innerText = "Raid Updated";
                messagePopup.classList.add('success');
                messagePopup.classList.remove('hidden');
            }
            this.dataStore.set('raid', response.data.raid);

            setTimeout(() => {
                this.redirectToViewRaid();
            }, 5000);


        } catch (error) {
            changeButton.innerText = origButtonText;
            messageText.innerText = `Error: ${error.message}`;
            messagePopup.classList.remove('hidden');
        }
    }



    getFormValues() {
        const raidName = document.getElementById('raid-name').value;
        const raidServer = document.getElementById('raid-server').value;
        const raidDate = document.getElementById('raid-date').value;
        const time = document.getElementById('raid-time').value;
        const raidSize = document.getElementById('raid-size').value;
        const raidObjective = document.getElementById('raid-objective').value;
        const lootDistribution = document.getElementById('loot-distribution').value;
        const dps = document.getElementById('dps-input').value;
        const healer = document.getElementById('healer-input').value;
        const tank = document.getElementById('tank-input').value;
        const status = document.getElementById('raid-status').value;
        const raidId = document.getElementById('raid-id').value;

        const requiredRoles = {
            DPS: dps,
            Healer: healer,
            Tank: tank
        };

        return {
            raidName,
            raidServer,
            raidDate,
            time,
            raidSize,
            raidObjective,
            lootDistribution,
            requiredRoles,
            status,
            raidId
        };
    }


    /**
     * Redirects the user to the ViewRaid page after updating or creating an event.
     */
    redirectToViewRaid() {
        const raid = this.dataStore.get('raid');
        const raidId = raid.raidId;

        if (raid != null) {
            window.location.href = `/viewRaid.html?id=${raidId}`;
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