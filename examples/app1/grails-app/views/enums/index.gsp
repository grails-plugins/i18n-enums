<!doctype html>
<html lang="en">
<head>
    <title>I18n Enums</title>
    <meta name="layout" content="main"/>
</head>

<body>
<main id="content" role="main" class="pb-4 pb-md-5">
    <div class="container-lg py-2 py-md-3">
        <h1 class="display-6 fw-semibold mb-2">I18n Enums</h1>
        <p class="text-body-secondary">
            Every row below is an enum constant resolved through Grails'
            <code>messageSource</code> as a <code>MessageSourceResolvable</code>.
            Current locale: <strong>${locale}</strong>.
        </p>

        <table class="table table-sm align-middle bg-body">
            <thead>
            <tr>
                <th scope="col">Enum constant</th>
                <th scope="col">Resolved message</th>
                <th scope="col">Default message</th>
                <th scope="col">Codes</th>
            </tr>
            </thead>
            <tbody>
            <g:each in="${resolvables}" var="resolvable">
                <tr>
                    <td><code>${resolvable.class.simpleName}.${resolvable.name}</code></td>
                    <td class="fw-medium"><g:message message="${resolvable}"/></td>
                    <td>${resolvable.defaultMessage}</td>
                    <td>
                        <g:each in="${resolvable.codes}" var="code" status="i">
                            <g:if test="${i > 0}"><br/></g:if><code class="small">${code}</code>
                        </g:each>
                    </td>
                </tr>
            </g:each>
            </tbody>
        </table>
    </div>
</main>
</body>
</html>
