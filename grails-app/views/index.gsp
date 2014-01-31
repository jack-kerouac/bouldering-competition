<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Home</title>
    <r:require module="index"/>
</head>

<body id="index-page">

<div class="row intro" ng-controller="StartCtrl">
    <div>
        <div class="story">
            <div>
                <h1><g:message code="bcomp.title"/></h1>

                <p><g:message
                        code="bcomp.title"/> bietet dir die Möglichkeit, deinen persönlichen Fortschritt beim Bouldern in der Halle
                zu verfolgen, zu visualisieren und mit anderen zu vergleichen.</p>


                <h2>Anmelden</h2>

                <p>Du willst das mal ausprobieren? Wir legen dir gerne auf Nachfrage einen Account an.
                    <a href="#" data-uv-trigger
                       data-uv-screenshot_enabled="false"
                       data-uv-contact_title="Account beantragen">
                        Schreib uns!
                    </a>
                </p>

                <p><g:message code="bcomp.title"/> ist bereits in folgenden Hallen verfügbar:</p>
                <ul><li ng-repeat="gym in gyms">
                    <span ng-bind="gym.name"></span>
                </li></ul>
            </div>
        </div>
    </div>

    <div>
        <div class="story">
            <h2>Warum?</h2>

            <p>Wir glauben daran, dass...</p>

            <ul>
                <li>du dich verbessern willst und dafür hart trainierst.</li>
                <li>das wichtigste Maß für Fortschritt in der Boulderleistung die Auswertung
                der Boulderprobleme sind, die du geschafft (und nicht geschafft) hast.</li>
                <li>mit einer hohen Wahrscheinlichkeit Bouldern selbst der beste Weg für dich ist,
                dich zu verbessern.</li>
            </ul>

        </div>
    </div>

    <div>
        <div class="story">
            <h2>Deswegen</h2>

            <p>Deswegen stellen wir dir mit <g:message code="bcomp.title"/> eine Website zur Seite...</p>
            <ul>
                <li>die dir neue Boulder in deinen Lieblingshallen empfiehlt und dich ermutigt mehr zu
                Bouldern.</li>
                <li>die dir anzeigt welche Boulder du in der Vergangenheit gemacht hast und wie sich darauf
                basierend dein Level entwickelt.</li>
                <li>mit der du deinen Fortschritt mit dem deiner Freunde vergleichen kannst.</li>
            </ul>
        </div>
    </div>
</div>

</body>
</html>