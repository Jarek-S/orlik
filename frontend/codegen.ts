import type {CodegenConfig} from '@graphql-codegen/cli';

const config: CodegenConfig = {
  schema: 'http://localhost:8080/graphql',
  documents: './src/**/*.ts',
  generates: {
    './src/app/graphql/generated.ts': {
      plugins: [
        'typescript',
        'typescript-operations',
        'typescript-apollo-angular'
      ],
      config: {
        addExplicitOverride: true,
      }
    }
  }
};
export default config;
