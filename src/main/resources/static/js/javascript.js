// ======================================================
// FESTPASS - COMPLETE JAVASCRIPT
// ======================================================

const API_BASE_URL = "http://localhost:8080";


// ======================================================
// GLOBAL DATA
// ======================================================

let events = [];
let attendees = [];
let bookings = [];
let tickets = [];


// ======================================================
// PAGE TITLES
// ======================================================

const pageInfo = {

    dashboard: {
        title: "Dashboard",
        subtitle: "Manage your college fest"
    },

    events: {
        title: "Events",
        subtitle: "Manage your college fest events"
    },

    attendees: {
        title: "Attendees",
        subtitle: "Manage fest attendees"
    },

    bookings: {
        title: "Bookings",
        subtitle: "Manage ticket bookings"
    },

    tickets: {
        title: "Tickets",
        subtitle: "Tickets and QR check-in"
    }

};


// ======================================================
// PAGE LOAD
// ======================================================

document.addEventListener("DOMContentLoaded", function () {

    console.log("FestPass JavaScript loaded");

    loadDashboard();

});


// ======================================================
// API HELPER
// ======================================================

async function apiRequest(url, options = {}) {

    try {

        const response = await fetch(
            API_BASE_URL + url,
            {
                ...options,
                headers: {
                    "Content-Type": "application/json",
                    ...(options.headers || {})
                }
            }
        );

        if (!response.ok) {

            let message = "Request failed: " + response.status;

            try {

                const errorData = await response.json();

                if (errorData.message) {
                    message = errorData.message;
                }

            } catch (e) {
                // Ignore JSON parsing error
            }

            throw new Error(message);
        }

        if (response.status === 204) {
            return null;
        }

        return await response.json();

    } catch (error) {

        console.error("API Error:", error);

        throw error;
    }
}


// ======================================================
// SHOW PAGE
// ======================================================

function showPage(page) {

    const pages = [
        "dashboard",
        "events",
        "attendees",
        "bookings",
        "tickets"
    ];

    pages.forEach(function (name) {

        const element =
            document.getElementById(name + "Page");

        if (element) {
            element.style.display =
                name === page ? "block" : "none";
        }

    });


    document.querySelectorAll(".nav-btn")
        .forEach(function (button) {

            button.classList.remove("active");

            if (button.dataset.page === page) {
                button.classList.add("active");
            }

        });


    const info = pageInfo[page];

    if (info) {

        document.getElementById("pageTitle")
            .textContent = info.title;

        document.getElementById("pageSubtitle")
            .textContent = info.subtitle;

    }


    // Load required data

    if (page === "dashboard") {
        loadDashboard();
    }

    if (page === "events") {
        loadEvents();
    }

    if (page === "attendees") {
        loadAttendees();
    }

    if (page === "bookings") {
        loadBookings();
    }

    if (page === "tickets") {
        loadTickets();
    }

}


// ======================================================
// DASHBOARD
// ======================================================

async function loadDashboard() {

    try {

        await Promise.all([
            loadEvents(),
            loadAttendees(),
            loadBookings(),
            loadTickets()
        ]);

        updateDashboardStats();

    } catch (error) {

        console.error(
            "Dashboard loading error:",
            error
        );

    }

}


// ======================================================
// UPDATE DASHBOARD STATS
// ======================================================

function updateDashboardStats() {

    const totalEvents =
        document.getElementById("totalEvents");

    const totalAttendees =
        document.getElementById("totalAttendees");

    const totalBookings =
        document.getElementById("totalBookings");

    const totalTickets =
        document.getElementById("totalTickets");


    if (totalEvents) {
        totalEvents.textContent =
            events.length;
    }

    if (totalAttendees) {
        totalAttendees.textContent =
            attendees.length;
    }

    if (totalBookings) {
        totalBookings.textContent =
            bookings.length;
    }

    if (totalTickets) {
        totalTickets.textContent =
            tickets.length;
    }


    renderDashboardEvents();

}


// ======================================================
// EVENTS
// ======================================================

async function loadEvents() {

    const tbody =
        document.getElementById("eventsTable");

    try {

        const data =
            await apiRequest("/events");

        events =
            Array.isArray(data)
                ? data
                : [];

        renderEvents(events);

        renderDashboardEvents();

        updateDashboardStats();

        return events;

    } catch (error) {

        console.error(
            "Unable to load events:",
            error
        );

        if (tbody) {

            tbody.innerHTML = `
                <tr>
                    <td colspan="6"
                        class="empty">
                        Unable to load events
                    </td>
                </tr>
            `;

        }

        return [];
    }

}


// ======================================================
// RENDER EVENTS
// ======================================================

function renderEvents(data) {

    const tbody =
        document.getElementById("eventsTable");

    if (!tbody) {
        return;
    }

    tbody.innerHTML = "";


    if (!data || data.length === 0) {

        tbody.innerHTML = `
            <tr>
                <td colspan="6"
                    class="empty">
                    No events found
                </td>
            </tr>
        `;

        return;
    }


    data.forEach(function (event) {

        const id =
            event.id ?? "-";

        const name =
            event.name ??
            event.eventName ??
            "-";

        const capacity =
            event.capacity ?? 0;

        const price =
            event.ticketPrice ??
            event.price ??
            0;

        const date =
            event.eventDate ??
            event.date ??
            "-";


        const row =
            document.createElement("tr");


        row.innerHTML = `

            <td>
                #${escapeHTML(id)}
            </td>

            <td>
                <strong>
                    ${escapeHTML(name)}
                </strong>
            </td>

            <td>
                ${escapeHTML(capacity)}
            </td>

            <td>
                ₹${formatPrice(price)}
            </td>

            <td>
                ${formatDate(date)}
            </td>

            <td>

                <button
                    class="btn btn-red"
                    onclick="deleteEvent(${id})">
                    Delete
                </button>

            </td>

        `;


        tbody.appendChild(row);

    });

}


// ======================================================
// DASHBOARD EVENTS
// ======================================================

function renderDashboardEvents() {

    const tbody =
        document.getElementById("dashboardEvents");

    if (!tbody) {
        return;
    }

    tbody.innerHTML = "";


    if (!events || events.length === 0) {

        tbody.innerHTML = `
            <tr>
                <td colspan="5"
                    class="empty">
                    No events found
                </td>
            </tr>
        `;

        return;
    }


    events.slice(0, 5)
        .forEach(function (event) {

            const id =
                event.id ?? "-";

            const name =
                event.name ??
                event.eventName ??
                "-";

            const capacity =
                event.capacity ?? 0;

            const price =
                event.ticketPrice ??
                event.price ??
                0;

            const date =
                event.eventDate ??
                event.date ??
                "-";


            const row =
                document.createElement("tr");


            row.innerHTML = `

                <td>#${escapeHTML(id)}</td>

                <td>
                    ${escapeHTML(name)}
                </td>

                <td>
                    ${escapeHTML(capacity)}
                </td>

                <td>
                    ₹${formatPrice(price)}
                </td>

                <td>
                    ${formatDate(date)}
                </td>

            `;


            tbody.appendChild(row);

        });

}


// ======================================================
// SEARCH EVENTS
// ======================================================

function filterEvents() {

    const input =
        document.getElementById("eventSearch");

    if (!input) {
        return;
    }

    const search =
        input.value
            .toLowerCase()
            .trim();


    const filtered =
        events.filter(function (event) {

            const name =
                String(
                    event.name ??
                    event.eventName ??
                    ""
                ).toLowerCase();

            return name.includes(search);

        });


    renderEvents(filtered);

}


// ======================================================
// OPEN EVENT MODAL
// ======================================================

function openEventModal() {

    document
        .getElementById("eventModal")
        .classList.add("show");

}


// ======================================================
// CLOSE EVENT MODAL
// ======================================================

function closeEventModal() {

    document
        .getElementById("eventModal")
        .classList.remove("show");

}


// ======================================================
// SAVE EVENT
// ======================================================

async function saveEvent(event) {

    event.preventDefault();


    const eventName =
        document
            .getElementById("eventName")
            .value
            .trim();

    const capacity =
        Number(
            document
                .getElementById("capacity")
                .value
        );

    const ticketPrice =
        Number(
            document
                .getElementById("ticketPrice")
                .value
        );

    const eventDate =
        document
            .getElementById("eventDate")
            .value;


    const data = {

        name: eventName,

        capacity: capacity,

        ticketPrice: ticketPrice,

        eventDate: eventDate

    };


    try {

        await apiRequest(
            "/events",
            {
                method: "POST",
                body: JSON.stringify(data)
            }
        );


        showToast(
            "Event created successfully!",
            "success"
        );


        document
            .getElementById("eventName")
            .value = "";

        document
            .getElementById("capacity")
            .value = "";

        document
            .getElementById("ticketPrice")
            .value = "";

        document
            .getElementById("eventDate")
            .value = "";


        closeEventModal();

        await loadEvents();

    } catch (error) {

        showToast(
            error.message ||
            "Unable to create event",
            "error"
        );

    }

}


// ======================================================
// DELETE EVENT
// ======================================================

async function deleteEvent(id) {

    if (!confirm(
        "Are you sure you want to delete this event?"
    )) {
        return;
    }


    try {

        await apiRequest(
            "/events/" + id,
            {
                method: "DELETE"
            }
        );


        showToast(
            "Event deleted successfully!",
            "success"
        );


        await loadEvents();

    } catch (error) {

        showToast(
            error.message ||
            "Unable to delete event",
            "error"
        );

    }

}


// ======================================================
// ATTENDEES
// ======================================================

async function loadAttendees() {

    const tbody =
        document.getElementById("attendeesTable");

    try {

        const data =
            await apiRequest("/attendees");

        attendees =
            Array.isArray(data)
                ? data
                : [];

        renderAttendees(attendees);

        updateDashboardStats();

        return attendees;

    } catch (error) {

        console.error(
            "Unable to load attendees:",
            error
        );


        if (tbody) {

            tbody.innerHTML = `
                <tr>
                    <td colspan="5"
                        class="empty">
                        Unable to load attendees
                    </td>
                </tr>
            `;

        }

        return [];
    }

}


// ======================================================
// RENDER ATTENDEES
// ======================================================

function renderAttendees(data) {

    const tbody =
        document.getElementById("attendeesTable");

    if (!tbody) {
        return;
    }

    tbody.innerHTML = "";


    if (!data || data.length === 0) {

        tbody.innerHTML = `
            <tr>
                <td colspan="5"
                    class="empty">
                    No attendees found
                </td>
            </tr>
        `;

        return;
    }


    data.forEach(function (attendee) {

        const id =
            attendee.id ?? "-";

        const name =
            attendee.name ?? "-";

        const email =
            attendee.email ?? "-";

        const phone =
            attendee.phone ?? "-";


        const row =
            document.createElement("tr");


        row.innerHTML = `

            <td>
                #${escapeHTML(id)}
            </td>

            <td>
                <strong>
                    ${escapeHTML(name)}
                </strong>
            </td>

            <td>
                ${escapeHTML(email)}
            </td>

            <td>
                ${escapeHTML(phone)}
            </td>

            <td>

                <button
                    class="btn btn-red"
                    onclick="deleteAttendee(${id})">
                    Delete
                </button>

            </td>

        `;


        tbody.appendChild(row);

    });

}


// ======================================================
// SEARCH ATTENDEES
// ======================================================

function filterAttendees() {

    const input =
        document.getElementById("attendeeSearch");

    if (!input) {
        return;
    }


    const search =
        input.value
            .toLowerCase()
            .trim();


    const filtered =
        attendees.filter(function (attendee) {

            const name =
                String(
                    attendee.name ?? ""
                ).toLowerCase();

            const email =
                String(
                    attendee.email ?? ""
                ).toLowerCase();

            const phone =
                String(
                    attendee.phone ?? ""
                ).toLowerCase();


            return (
                name.includes(search) ||
                email.includes(search) ||
                phone.includes(search)
            );

        });


    renderAttendees(filtered);

}


// ======================================================
// OPEN ATTENDEE MODAL
// ======================================================

function openAttendeeModal() {

    document
        .getElementById("attendeeModal")
        .classList.add("show");

}


// ======================================================
// CLOSE ATTENDEE MODAL
// ======================================================

function closeAttendeeModal() {

    document
        .getElementById("attendeeModal")
        .classList.remove("show");

}


// ======================================================
// SAVE ATTENDEE
// ======================================================

async function saveAttendee(event) {

    event.preventDefault();


    const name =
        document
            .getElementById("attendeeName")
            .value
            .trim();

    const email =
        document
            .getElementById("attendeeEmail")
            .value
            .trim();

    const phone =
        document
            .getElementById("attendeePhone")
            .value
            .trim();


    const data = {

        name: name,

        email: email,

        phone: phone

    };


    try {

        await apiRequest(
            "/attendees",
            {
                method: "POST",
                body: JSON.stringify(data)
            }
        );


        showToast(
            "Attendee added successfully!",
            "success"
        );


        document
            .getElementById("attendeeName")
            .value = "";

        document
            .getElementById("attendeeEmail")
            .value = "";

        document
            .getElementById("attendeePhone")
            .value = "";


        closeAttendeeModal();

        await loadAttendees();

    } catch (error) {

        showToast(
            error.message ||
            "Unable to add attendee",
            "error"
        );

    }

}


// ======================================================
// DELETE ATTENDEE
// ======================================================

async function deleteAttendee(id) {

    if (!confirm(
        "Are you sure you want to delete this attendee?"
    )) {
        return;
    }


    try {

        await apiRequest(
            "/attendees/" + id,
            {
                method: "DELETE"
            }
        );


        showToast(
            "Attendee deleted successfully!",
            "success"
        );


        await loadAttendees();

    } catch (error) {

        showToast(
            error.message ||
            "Unable to delete attendee",
            "error"
        );

    }

}


// ======================================================
// BOOKINGS
// ======================================================

async function loadBookings() {

    const tbody =
        document.getElementById("bookingsTable");

    try {

        const data =
            await apiRequest("/bookings");

        bookings =
            Array.isArray(data)
                ? data
                : [];

        renderBookings(bookings);

        updateDashboardStats();

        return bookings;

    } catch (error) {

        console.error(
            "Unable to load bookings:",
            error
        );


        if (tbody) {

            tbody.innerHTML = `
                <tr>
                    <td colspan="6"
                        class="empty">
                        Unable to load bookings
                    </td>
                </tr>
            `;

        }

        return [];
    }

}


// ======================================================
// RENDER BOOKINGS
// ======================================================

function renderBookings(data) {

    const tbody =
        document.getElementById("bookingsTable");

    if (!tbody) {
        return;
    }

    tbody.innerHTML = "";


    if (!data || data.length === 0) {

        tbody.innerHTML = `
            <tr>
                <td colspan="6"
                    class="empty">
                    No bookings found
                </td>
            </tr>
        `;

        return;
    }


    data.forEach(function (booking) {

        const id =
            booking.id ?? "-";


        const attendee =
            booking.attendee?.name ??
            booking.attendeeName ??
            "-";


        const event =
            booking.event?.name ??
            booking.event?.eventName ??
            booking.eventName ??
            "-";


        const ticketCount =
            booking.numberOfTickets ??
            booking.tickets ??
            0;


        const status =
            booking.status ??
            "Confirmed";


        const row =
            document.createElement("tr");


        row.innerHTML = `

            <td>
                #${escapeHTML(id)}
            </td>

            <td>
                ${escapeHTML(attendee)}
            </td>

            <td>
                ${escapeHTML(event)}
            </td>

            <td>
                ${escapeHTML(ticketCount)}
            </td>

            <td>
                <span class="badge badge-green">
                    ${escapeHTML(status)}
                </span>
            </td>

            <td>

                <button
                    class="btn btn-red"
                    onclick="deleteBooking(${id})">
                    Delete
                </button>

            </td>

        `;


        tbody.appendChild(row);

    });

}


// ======================================================
// OPEN BOOKING MODAL
// ======================================================

function openBookingModal() {

    document
        .getElementById("bookingModal")
        .classList.add("show");

}


// ======================================================
// CLOSE BOOKING MODAL
// ======================================================

function closeBookingModal() {

    document
        .getElementById("bookingModal")
        .classList.remove("show");

}


// ======================================================
// SAVE BOOKING
// ======================================================

async function saveBooking(event) {

    event.preventDefault();


    const attendeeId =
        Number(
            document
                .getElementById("bookingAttendeeId")
                .value
        );


    const eventId =
        Number(
            document
                .getElementById("bookingEventId")
                .value
        );


    const numberOfTickets =
        Number(
            document
                .getElementById("numberOfTickets")
                .value
        );


    const data = {

        attendeeId: attendeeId,

        eventId: eventId,

        numberOfTickets: numberOfTickets

    };


    try {

        await apiRequest(
            "/bookings",
            {
                method: "POST",
                body: JSON.stringify(data)
            }
        );


        showToast(
            "Booking created successfully!",
            "success"
        );


        document
            .getElementById("bookingAttendeeId")
            .value = "";

        document
            .getElementById("bookingEventId")
            .value = "";

        document
            .getElementById("numberOfTickets")
            .value = "1";


        closeBookingModal();

        await loadBookings();

        await loadTickets();

    } catch (error) {

        showToast(
            error.message ||
            "Unable to create booking",
            "error"
        );

    }

}


// ======================================================
// DELETE BOOKING
// ======================================================

async function deleteBooking(id) {

    if (!confirm(
        "Are you sure you want to delete this booking?"
    )) {
        return;
    }


    try {

        await apiRequest(
            "/bookings/" + id,
            {
                method: "DELETE"
            }
        );


        showToast(
            "Booking deleted successfully!",
            "success"
        );


        await loadBookings();

        await loadTickets();

    } catch (error) {

        showToast(
            error.message ||
            "Unable to delete booking",
            "error"
        );

    }

}


// ======================================================
// TICKETS
// ======================================================

async function loadTickets() {

    /*
     * IMPORTANT:
     * HTML id = ticketsTable
     *
     * Old JS was using ticketsTableBody.
     * That mismatch caused the ticket table problem.
     */

    const tbody =
        document.getElementById("ticketsTable");


    if (!tbody) {

        console.error(
            "ticketsTable not found"
        );

        return;
    }


    tbody.innerHTML = `

        <tr>

            <td colspan="7"
                class="empty">

                Loading tickets...

            </td>

        </tr>

    `;


    try {

        console.log(
            "GET:",
            API_BASE_URL + "/tickets"
        );


        const data =
            await apiRequest("/tickets");


        tickets =
            Array.isArray(data)
                ? data
                : [];


        console.log(
            "Tickets received:",
            tickets
        );


        renderTickets(tickets);

        updateDashboardStats();


    } catch (error) {

        console.error(
            "Unable to load tickets:",
            error
        );


        tbody.innerHTML = `

            <tr>

                <td colspan="7"
                    class="empty">

                    Unable to load tickets

                    <br>

                    <small>
                        ${escapeHTML(
            error.message ||
            "Backend error"
        )}
                    </small>

                </td>

            </tr>

        `;

    }

}


// ======================================================
// RENDER TICKETS
// ======================================================

function renderTickets(data) {

    const tbody =
        document.getElementById("ticketsTable");


    if (!tbody) {

        console.error(
            "ticketsTable not found"
        );

        return;
    }


    tbody.innerHTML = "";


    if (!data || data.length === 0) {

        tbody.innerHTML = `

            <tr>

                <td colspan="7"
                    class="empty">

                    No tickets found

                </td>

            </tr>

        `;

        return;
    }


    data.forEach(function (ticket) {

        const id =
            ticket.id ?? "-";


        const ticketNumber =
            ticket.ticketNumber ??
            "-";


        const ticketType =
            ticket.ticketType ??
            "GENERAL";


        const price =
            ticket.price ??
            0;


        const qrCode =
            ticket.qrCode ??
            "";


        const checkedIn =
            ticket.checkedIn === true;


        let statusHTML;


        if (checkedIn) {

            statusHTML = `

                <span class="badge badge-green">
                    Checked In
                </span>

            `;

        } else {

            statusHTML = `

                <span class="badge badge-red">
                    Not Checked In
                </span>

            `;

        }


        let qrButton;


        if (qrCode) {

            qrButton = `

                <button
                    class="btn btn-purple"
                    onclick="showQR('${escapeForAttribute(qrCode)}')">

                    QR

                </button>

            `;

        } else {

            qrButton = `

                <span style="color:#999;">
                    No QR
                </span>

            `;

        }


        let checkInButton;


        if (checkedIn) {

            checkInButton = `

                <button
                    class="btn btn-gray"
                    disabled>

                    Checked In

                </button>

            `;

        } else if (qrCode) {

            checkInButton = `

                <button
                    class="btn btn-blue"
                    onclick="checkInTicket('${escapeForAttribute(qrCode)}')">

                    Check In

                </button>

            `;

        } else {

            checkInButton = `

                <button
                    class="btn btn-gray"
                    disabled>

                    Check In

                </button>

            `;

        }


        const row =
            document.createElement("tr");


        row.innerHTML = `

            <td>
                #${escapeHTML(id)}
            </td>

            <td>
                <strong>
                    ${escapeHTML(ticketNumber)}
                </strong>
            </td>

            <td>
                ${escapeHTML(ticketType)}
            </td>

            <td>
                ₹${formatPrice(price)}
            </td>

            <td>
                ${qrButton}
            </td>

            <td>
                ${statusHTML}
            </td>

            <td>
                ${checkInButton}
            </td>

        `;


        tbody.appendChild(row);

    });

}


// ======================================================
// SHOW QR
// ======================================================

function showQR(qrCode) {

    if (!qrCode) {

        showToast(
            "QR code not available",
            "error"
        );

        return;
    }


    const modal =
        document.getElementById("qrModal");

    const ticketNumber =
        document.getElementById("qrTicketNumber");

    const qrImage =
        document.getElementById("qrImage");


    ticketNumber.textContent =
        qrCode;


    qrImage.src =
        "https://api.qrserver.com/v1/create-qr-code/?size=220x220&data="
        + encodeURIComponent(qrCode);


    modal.classList.add("show");

}


// ======================================================
// CLOSE QR MODAL
// ======================================================

function closeQRModal() {

    const modal =
        document.getElementById("qrModal");

    if (modal) {
        modal.classList.remove("show");
    }

}


// ======================================================
// CHECK IN TICKET
// ======================================================

async function checkInTicket(qrCode) {

    if (!qrCode) {

        showToast(
            "Invalid QR code",
            "error"
        );

        return;
    }


    const confirmCheckIn =
        confirm(
            "Are you sure you want to check in this ticket?"
        );


    if (!confirmCheckIn) {
        return;
    }


    try {

        console.log(
            "Checking in:",
            qrCode
        );


        /*
         * This endpoint must match your
         * TicketController mapping.
         */

        const updatedTicket =
            await apiRequest(
                "/tickets/check-in/" +
                encodeURIComponent(qrCode),
                {
                    method: "PUT"
                }
            );


        console.log(
            "Checked in:",
            updatedTicket
        );


        showToast(
            "Ticket checked in successfully!",
            "success"
        );


        await loadTickets();


    } catch (error) {

        console.error(
            "Check-in error:",
            error
        );


        showToast(
            error.message ||
            "Unable to check in ticket",
            "error"
        );

    }

}


// ======================================================
// REFRESH TICKETS
// ======================================================

function refreshTickets() {

    loadTickets();

}


// ======================================================
// TOAST
// ======================================================

function showToast(message, type) {

    const toast =
        document.getElementById("toast");


    if (!toast) {
        return;
    }


    toast.textContent =
        message;


    toast.className =
        "toast show " +
        (type || "success");


    setTimeout(function () {

        toast.className =
            "toast";

    }, 3000);

}


// ======================================================
// FORMAT PRICE
// ======================================================

function formatPrice(value) {

    const number =
        Number(value);


    if (Number.isNaN(number)) {
        return "0.00";
    }


    return number.toFixed(2);

}


// ======================================================
// FORMAT DATE
// ======================================================

function formatDate(value) {

    if (!value || value === "-") {
        return "-";
    }


    try {

        const date =
            new Date(value);


        if (Number.isNaN(
            date.getTime()
        )) {
            return value;
        }


        return date.toLocaleString(
            "en-IN",
            {
                dateStyle: "medium",
                timeStyle: "short"
            }
        );

    } catch (error) {

        return value;

    }

}


// ======================================================
// HTML ESCAPE
// ======================================================

function escapeHTML(value) {

    if (
        value === null ||
        value === undefined
    ) {
        return "";
    }


    return String(value)

        .replace(
            /&/g,
            "&amp;"
        )

        .replace(
            /</g,
            "&lt;"
        )

        .replace(
            />/g,
            "&gt;"
        )

        .replace(
            /"/g,
            "&quot;"
        )

        .replace(
            /'/g,
            "&#039;"
        );

}


// ======================================================
// ATTRIBUTE ESCAPE
// ======================================================

function escapeForAttribute(value) {

    if (
        value === null ||
        value === undefined
    ) {
        return "";
    }


    return String(value)

        .replace(
            /\\/g,
            "\\\\"
        )

        .replace(
            /'/g,
            "\\'"
        )

        .replace(
            /"/g,
            "&quot;"
        );

}


// ======================================================
// CLOSE MODALS WHEN CLICKING OUTSIDE
// ======================================================

document.addEventListener(
    "click",
    function (event) {

        if (
            event.target.classList.contains(
                "modal-overlay"
            )
        ) {

            event.target.classList.remove(
                "show"
            );

        }

    }
);