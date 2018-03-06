workspace(name = "com_github_pmbethe09_dtac")

PROTO_VERS = "3.5.1"

PROTO_SHA = "1f8b9b202e9a4e467ff0b0f25facb1642727cdf5e69092038f15b37c75b99e45"

http_archive(
    name = "com_google_protobuf",
    sha256 = PROTO_SHA,
    strip_prefix = "protobuf-" + PROTO_VERS,
    urls = ["https://github.com/google/protobuf/archive/v" + PROTO_VERS + ".zip"],
)

http_archive(
    name = "com_github_google_protobuf",
    sha256 = PROTO_SHA,
    strip_prefix = "protobuf-" + PROTO_VERS,
    urls = ["https://github.com/google/protobuf/archive/v" + PROTO_VERS + ".zip"],
)

new_git_repository(
    name = "com_github_google_googletest",
    build_file = "third_party/BUILD.gtest",
    remote = "https://github.com/google/googletest",
    tag = "release-1.8.0",
)

bind(
    name = "gtest",
    actual = "@com_github_google_googletest//:gtest",
)

maven_jar(
    name = "aopalliance_repo",
    artifact = "aopalliance:aopalliance:1.0",
    sha1 = "0235ba8b489512805ac13a8f9ea77a1ca5ebe3e8",
)

maven_jar(
    name = "auto_value_repo",
    artifact = "com.google.auto.value:auto-value:1.5.3",
    sha1 = "514df6a7c7938de35c7f68dc8b8f22df86037f38",
)

maven_jar(
    name = "guava_repo",
    artifact = "com.google.guava:guava:24.0-jre",
    sha1 = "041ac1e74d6b4e1ea1f027139cffeb536c732a81",
)

maven_jar(
    name = "guice_repo",
    artifact = "com.google.inject:guice:4.2.0",
    sha1 = "25e1f4c1d528a1cffabcca0d432f634f3132f6c8",
)

maven_jar(
    name = "guice_assistedinject_repo",
    artifact = "com.google.inject.extensions:guice-assistedinject:4.2.0",
    sha1 = "e7270305960ad7db56f7e30cb9df6be9ff1cfb45",
)

maven_jar(
    name = "jsr305_repo",
    artifact = "com.google.code.findbugs:jsr305:3.0.1",
    sha1 = "f7be08ec23c21485b9b5a1cf1654c2ec8c58168d",
)

maven_jar(
    name = "jsr330_repo",
    artifact = "javax.inject:javax.inject:1",
    sha1 = "6975da39a7040257bd51d21a231b76c915872d38",
)
