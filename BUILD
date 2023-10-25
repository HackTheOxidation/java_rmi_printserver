load("@rules_java//java:defs.bzl", "java_binary", "java_library")

package(default_visibility = ["//visibility:public"])

java_library(
        name = "PrintServer.Common",
        srcs = glob(["src/main/java/printserver/common/*.java"]),
)

java_binary(
        name = "PrintServer.Server",
        srcs = glob(["src/main/java/printserver/server/*.java"]),
        main_class = "printserver.server.App",
        deps = ["//:PrintServer.Common", "postgresql-42.6.0.jar"],
)

java_binary(
        name = "PrintServer.Client",
        srcs = glob(["src/main/java/printserver/client/*.java"]),
        main_class = "printserver.client.Client",
        deps = ["//:PrintServer.Common"],
)
