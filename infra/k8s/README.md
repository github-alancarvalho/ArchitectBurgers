
## Local Deployment (Minikube)

Preparação minikube (1 vez apenas)

    # Criar diretório do volume local
	minikube ssh -- sudo mkdir -p /data/pg-data-volume

    # Habilitar addon metrics server
    minikube addons enable metrics-server

Deploy:

    kubectl apply -f ./db/db-configs.yml
    kubectl apply -f ./db/data-volume.yml
    kubectl apply -f ./db/data-volume-claim.yml
    kubectl apply -f ./db/db-deployment.yml
    kubectl apply -f ./db/db-service.yml
    kubectl apply -f ./app/pagamento-configs.yml
    kubectl apply -f ./app/app-deployment.yml
    kubectl apply -f ./app/app-service-localcluster.yml
    kubectl apply -f ./app/app-hpa.yml

## Cloud Deployment

Criar a infraestrutura com os dois passos abaixo:
- Executar o setup na pasta terraform
- Cluster EKS ainda é uma tarefa manual. Criar pelo console AWS assim como o NodeGroup, e configurar o kubectl

Iniciar os elementos no cluster Kubernetes:

    kubectl apply -f ./app/app-service-loadbalancer.yml

    # Antes do comando abaixo: Ajustar os dados de conexão no db-configs-cloud a partir da instância RDS
    kubectl apply -f ./db/db-configs-cloud.yml
    
    # Antes do comando abaixo: Preencher o endpoint do LoadBalancer (kubectl get svc -> External IP) 
    # na config do webhook em pagamento-configs
    kubectl apply -f ./app/pagamento-configs.yml

    kubectl apply -f ./app/app-deployment.yml 
    kubectl apply -f ./app/app-hpa.yml

