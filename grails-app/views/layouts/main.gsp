<!DOCTYPE html>
<!--[if IE 9]><html class="lt-ie10" lang="en" > <![endif]-->
<html class="no-js" lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title><g:layoutTitle default="Grails"/></title>
    <link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon.ico')}" type="image/x-icon">
    <link rel="apple-touch-icon" href="${resource(dir: 'images', file: 'apple-touch-icon.png')}">
    <link rel="apple-touch-icon" sizes="114x114" href="${resource(dir: 'images', file: 'apple-touch-icon-retina.png')}">
    <!-- If you are using CSS version, only link these 2 files, you may add app.css to use for your overrides if you like. -->
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'normalize.css')}">
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'foundation.css')}">
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'foundation-datepicker.css')}">
    <script src="js/vendor/custom.modernizr.js"></script>
    <g:layoutHead/>
    <g:javascript library="application"/>
    <r:layoutResources/>
</head>

<body>
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
                    <a href="#">florian</a>
                    <ul class="dropdown">
                        <li><a href="#">switch to Christoph</a></li>
                    </ul>
                </li>
            </ul>

            <!-- Left Nav Section -->
            <ul class="left">
                <li><a href="#">Heavens Gate</a></li>
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

<div id="spinner" class="spinner" style="display:none;"><g:message code="spinner.alt" default="Loading&hellip;"/></div>
<r:layoutResources/>
</body>
</html>