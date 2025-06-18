title ${project.build.finalName} ConsoleManager

chcp 65001

cls

:end

java -server -jar lib/${project.build.finalName}.jar

goto end
