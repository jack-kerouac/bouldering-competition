<%@ page import="org.springframework.web.servlet.support.RequestContextUtils" %>
<!DOCTYPE html>
<!--[if IE 9]><html class="lt-ie10" lang="en" > <![endif]-->
<html class="no-js" lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title><g:layoutTitle default="Grails"/> - Bouldering Competition</title>
    <link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon.ico')}" type="image/x-icon">
    <link rel="apple-touch-icon" href="${resource(dir: 'images', file: 'apple-touch-icon.png')}">
    <link rel="apple-touch-icon" sizes="114x114" href="${resource(dir: 'images', file: 'apple-touch-icon-retina.png')}">
    <!-- If you are using CSS version, only link these 2 files, you may add app.css to use for your overrides if you like. -->
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'normalize.css')}">
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'foundation.css')}">
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'foundation-icons.css')}">
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'leaflet.css')}">
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'application.css')}">
    <script src="${resource(dir: 'js', file: 'vendor/custom.modernizr.js')}"></script>
    <g:layoutHead/>
    <g:javascript library="application"/>
    <r:layoutResources/>
</head>

<body id="${pageProperty(name: 'body.id')}">
<div class="row">
    <nav class="top-bar fixed contain-to-grid" data-topbar>
        <ul class="title-area">
            <li class="name">
                <h1><g:link controller="home">BComp</g:link></h1>
            </li>
            <li class="toggle-topbar menu-icon"><a href="#"><span>Menu</span></a></li>
        </ul>

        <section class="top-bar-section">
            <!-- Right Nav Section -->
            <ul class="right">
                <li class="has-dropdown">
                    <a href="#"><sec:loggedInUserInfo field="username"/></a>
                    <ul class="dropdown">
                        <li><g:link controller='boulderer' action='listAscents' params="[username:
                                sec.loggedInUserInfo(field: 'username')]">
                                <g:message code="bcomp.ascents.label"/>
                            </g:link></li>
                        <li><g:link controller='boulderer' action='statistics' params="[username:
                                sec.loggedInUserInfo(field: 'username')]">
                            <g:message code="bcomp.statistics.label"/>
                        </g:link></li>
                        <li><g:link controller='logout'><g:message code="default.button.logout.label"/></g:link></li>
                    </ul>
                </li>
            </ul>

            <!-- Left Nav Section -->
            <ul class="left">
                %{--<li><a href="#">Boulderwelt</a></li>--}%
            </ul>
        </section>
    </nav>
</div>
<g:layoutBody/>

<div class="row">
    <div class="small-12">
        <flashMsg:msgBody key="confirm">
            <div data-alert class="alert-box success">
                ${it}
                <a href="#" class="close">&times;</a>
            </div>
        </flashMsg:msgBody>
        <flashMsg:msgBody key="error">
            <div data-alert class="alert-box warning">
                ${it}
                <a href="#" class="close">&times;</a>
            </div>
        </flashMsg:msgBody>
    </div>
</div>

<div class="footer" role="contentinfo"></div>

<script>
    var locale = '${RequestContextUtils.getLocale(request).toString().replace('_', '-')}';
</script>

<r:layoutResources/>

<script>
    // Include the UserVoice JavaScript SDK (only needed once on a page)
    UserVoice=window.UserVoice||[];(function(){var uv=document.createElement('script');uv.type='text/javascript';uv.async=true;uv.src='//widget.uservoice.com/2oIoDbuTLfxbQe92GPJLAA.js';var s=document.getElementsByTagName('script')[0];s.parentNode.insertBefore(uv,s)})();

    //
    // UserVoice Javascript SDK developer documentation:
    // https://www.uservoice.com/o/javascript-sdk
    //

    // Set colors
    UserVoice.push(['set', {
        accent_color: '#448dd6',
        trigger_color: 'white',
        trigger_background_color: '#e2753a'
    }]);

    // Identify the user and pass traits
    // To enable, replace sample data with actual user traits and uncomment the line
    UserVoice.push(['identify', {
        <sec:ifLoggedIn>
            name: '${sec.loggedInUserInfo(field: 'username')}',
            id: '${sec.loggedInUserInfo(field: 'id')}'
        </sec:ifLoggedIn>

        //email:      'john.doe@example.com', // User’s email address
        //name:       'John Doe', // User’s real name
        //created_at: 1364406966, // Unix timestamp for the date the user signed up
        //id:         123, // Optional: Unique id of the user (if set, this should not change)
        //type:       'Owner', // Optional: segment your users by type
        //account: {
        //  id:           123, // Optional: associate multiple users with a single account
        //  name:         'Acme, Co.', // Account name
        //  created_at:   1364406966, // Unix timestamp for the date the account was created
        //  monthly_rate: 9.99, // Decimal; monthly rate of the account
        //  ltv:          1495.00, // Decimal; lifetime value of the account
        //  plan:         'Enhanced' // Plan name for the account
        //}
    }]);

    // Add default trigger to the bottom-right corner of the window:
    UserVoice.push(['addTrigger', { mode: 'contact', trigger_position: 'bottom-right' }]);

    // Or, use your own custom trigger:
    //UserVoice.push(['addTrigger', '#id', { mode: 'contact' }]);

    // Autoprompt for Satisfaction and SmartVote (only displayed under certain conditions)
    UserVoice.push(['autoprompt', {}]);
</script>


<!-- Google Analytics -->
<script>
    (function (b, o, i, l, e, r) {
        b.GoogleAnalyticsObject = l;
        b[l] || (b[l] =
                function () {
                    (b[l].q = b[l].q || []).push(arguments)
                });
        b[l].l = +new Date;
        e = o.createElement(i);
        r = o.getElementsByTagName(i)[0];
        e.src = '//www.google-analytics.com/analytics.js';
        r.parentNode.insertBefore(e, r)
    }(window, document, 'script', 'ga'));
    ga('create', 'UA-47002539-1');
    ga('send', 'pageview');
</script>

</body>
</html>